package kr.toxicity.advancedskills.nms.v1_20_R3

import com.mojang.math.Transformation
import kr.toxicity.advancedskills.api.AdvancedSkills
import kr.toxicity.advancedskills.api.nms.NMS
import kr.toxicity.advancedskills.api.nms.NMSVersion
import kr.toxicity.advancedskills.api.nms.VirtualBlockDisplay
import kr.toxicity.advancedskills.api.nms.VirtualDisplay
import kr.toxicity.advancedskills.api.nms.VirtualItemDisplay
import kr.toxicity.advancedskills.api.nms.VirtualTextDisplay
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.Brightness
import net.minecraft.world.entity.Display
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.phys.Vec3
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.data.BlockData
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld
import org.bukkit.craftbukkit.v1_20_R3.block.data.CraftBlockData
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack
import org.bukkit.craftbukkit.v1_20_R3.util.CraftChatMessage
import org.bukkit.entity.Entity
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import org.joml.Vector3f
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class NMSImpl: NMS {
    override fun version(): NMSVersion = NMSVersion.V1_20_R3

    override fun createFakeEntity(world: World): Entity = ArmorStand(EntityType.ARMOR_STAND, (world as CraftWorld).handle).apply {
        setPos(Vec3(
            Double.MAX_VALUE,
            Double.MAX_VALUE,
            Double.MAX_VALUE,
        ))
    }.bukkitEntity

    override fun createBlock(world: World): VirtualBlockDisplay = VirtualBlockDisplayImpl(world)
    override fun createItem(world: World): VirtualItemDisplay = VirtualItemDisplayImpl(world)
    override fun createText(world: World): VirtualTextDisplay = VirtualTextDisplayImpl(world)

    private abstract class VirtualDisplayImpl<T: Display>(private val world: World, protected val t: T): VirtualDisplay {
        private val players = ConcurrentHashMap<UUID, ServerPlayer>()
        private val craftWorld = world as CraftWorld
        private val server = Bukkit.getServer()

        init {
            t.entityData.set(Display.DATA_POS_ROT_INTERPOLATION_DURATION_ID, 1)
            t.setPos(Vec3(
                Double.MAX_VALUE,
                Double.MAX_VALUE,
                Double.MAX_VALUE,
            ))
            update()
        }

        private var removed = false

        override fun world(): World = world

        private fun update() {

            val add = ClientboundAddEntityPacket(t)
            val remove = ClientboundRemoveEntitiesPacket(t.id)
            val teleport = ClientboundTeleportEntityPacket(t)

            val loc = t.location()

            val data = t.entityData.nonDefaultValues?.let {
                ClientboundSetEntityDataPacket(t.id, it)
            }

            players.values.removeIf {
                if (!it.valid || server.getPlayer(it.uuid) == null || it.location().distance(loc) > 30) {
                    it.connection.send(remove)
                    true
                } else {
                    it.connection.send(teleport)
                    false
                }
            }
            craftWorld.handle.entityLookup.all.forEach {
                if (it is ServerPlayer && it.valid && server.getPlayer(it.uuid) != null && it.location().distance(loc) <= 30) {
                    players.computeIfAbsent(it.uuid) { _ ->
                        it.connection.send(add)
                        data?.let { set ->
                            it.connection.send(set)
                        }
                        it
                    }
                }
            }
        }

        private val task = AdvancedSkills.inst().scheduler().asyncTaskTimer(1, 1) {
            update()
        }

        private fun net.minecraft.world.entity.Entity.location() = Location(
            craftWorld,
            x,
            y,
            z,
            bukkitYaw,
            xRot
        )

        override fun uuid(): UUID = t.uuid
        override fun location(): Location = t.location()

        override fun teleport(location: Location) {
            t.moveTo(
                location.x,
                location.y,
                location.z,
                location.yaw,
                location.pitch
            )
            val teleport = ClientboundTeleportEntityPacket(t)
            players.values.forEach {
                it.connection.send(teleport)
            }
        }

        protected fun sendDataPacket() {
            val data = t.entityData.nonDefaultValues?.let {
                ClientboundSetEntityDataPacket(t.id, it)
            } ?: return
            players.values.forEach {
                it.connection.send(data)
            }
        }

        override fun scale(vector: Vector) {
            t.setTransformation(Transformation(null, null, Vector3f(vector.x.toFloat(), vector.y.toFloat(), vector.z.toFloat()), null))
            sendDataPacket()
        }

        override fun brightness(brightness: org.bukkit.entity.Display.Brightness) {
            t.brightnessOverride = Brightness(brightness.blockLight, brightness.skyLight)
            sendDataPacket()
        }

        override fun billboard(billboard: org.bukkit.entity.Display.Billboard) {
            t.billboardConstraints = when (billboard) {
                org.bukkit.entity.Display.Billboard.CENTER -> Display.BillboardConstraints.CENTER
                org.bukkit.entity.Display.Billboard.HORIZONTAL -> Display.BillboardConstraints.HORIZONTAL
                org.bukkit.entity.Display.Billboard.VERTICAL -> Display.BillboardConstraints.VERTICAL
                org.bukkit.entity.Display.Billboard.FIXED -> Display.BillboardConstraints.FIXED
            }
            sendDataPacket()
        }

        override fun remove() {
            val remove = ClientboundRemoveEntitiesPacket(t.id)
            removed = true
            task.cancel()
            players.values.forEach {
                it.connection.send(remove)
            }
        }

        override fun isRemoved(): Boolean = removed
    }

    private class VirtualTextDisplayImpl(world: World): VirtualTextDisplay, VirtualDisplayImpl<Display.TextDisplay>(world, Display.TextDisplay(EntityType.TEXT_DISPLAY, (world as CraftWorld).handle).apply {
        entityData.set(Display.TextDisplay.DATA_BACKGROUND_COLOR_ID, 0)
    }) {
        override fun text(component: Component) {
            t.text = CraftChatMessage.fromJSON(GsonComponentSerializer.gson().serialize(component))
            sendDataPacket()
        }
        override fun opacity(opacity: Byte) {
            t.textOpacity = opacity
            sendDataPacket()
        }
    }

    private class VirtualItemDisplayImpl(world: World): VirtualItemDisplay, VirtualDisplayImpl<Display.ItemDisplay>(world, Display.ItemDisplay(EntityType.ITEM_DISPLAY, (world as CraftWorld).handle)) {
        override fun item(itemStack: ItemStack) {
            t.itemStack = CraftItemStack.asNMSCopy(itemStack)
            sendDataPacket()
        }
        override fun scale(vector: Vector) {
            t.setTransformation(Transformation(Vector3f(-0.5F, -0.5F, -0.5F), null, Vector3f(vector.x.toFloat(), vector.y.toFloat(), vector.z.toFloat()), null))
            sendDataPacket()
        }
    }

    private class VirtualBlockDisplayImpl(world: World): VirtualBlockDisplay, VirtualDisplayImpl<Display.BlockDisplay>(world, Display.BlockDisplay(EntityType.BLOCK_DISPLAY, (world as CraftWorld).handle)) {
        override fun block(data: BlockData) {
            t.blockState = (data as CraftBlockData).state
            sendDataPacket()
        }
        override fun scale(vector: Vector) {
            t.setTransformation(Transformation(Vector3f(-0.5F, -0.5F, -0.5F), null, Vector3f(vector.x.toFloat(), vector.y.toFloat(), vector.z.toFloat()), null))
            sendDataPacket()
        }
    }
}