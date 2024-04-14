package kr.toxicity.advancedskills.entity

import kr.toxicity.advancedskills.api.nms.VirtualDisplay
import kr.toxicity.advancedskills.util.PLUGIN
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import java.util.UUID

interface Trackable {
    val uuid: UUID
    val world: World
    val available: Boolean
    var location: Location
    val async: Boolean
    val isEmpty: Boolean

    fun remove()

    companion object {
        fun of(display: VirtualDisplay) = object : Trackable {
            override val uuid: UUID
                get() = display.uuid()
            override val world: World
                get() = display.world()
            override val available: Boolean
                get() = !display.isRemoved
            override var location: Location
                get() = display.location()
                set(value) {
                    display.teleport(value)
                }
            override val async: Boolean
                get() = true
            override val isEmpty: Boolean
                get() = false
            override fun remove() {
                display.remove()
            }
        }
        fun of(entity: Entity) = object : Trackable {
            override val uuid: UUID
                get() = entity.uniqueId
            override val world: World
                get() = entity.world
            override val available: Boolean
                get() = entity.isValid
            override var location: Location
                get() = entity.location
                set(value) {
                    PLUGIN.scheduler().teleport(entity, value)
                }
            override val async: Boolean
                get() = false
            override val isEmpty: Boolean
                get() = false
            override fun remove() {
                entity.remove()
            }
        }
        fun empty(world: World): Trackable {
            val uuid = UUID.randomUUID()
            var loc = Location(world, 0.0, 0.0, 0.0)
            var removed = false
            return object : Trackable {
                override val uuid: UUID
                    get() = uuid
                override val world: World
                    get() = world
                override val available: Boolean
                    get() = !removed && loc.chunk.isLoaded
                override var location: Location
                    get() = loc.clone()
                    set(value) {
                        loc = value
                    }
                override val async: Boolean
                    get() = true
                override val isEmpty: Boolean
                    get() = true
                override fun remove() {
                    removed = true
                }
            }
        }
    }
}