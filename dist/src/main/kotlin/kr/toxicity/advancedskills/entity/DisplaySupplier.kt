package kr.toxicity.advancedskills.entity

import kr.toxicity.advancedskills.api.nms.VirtualDisplay
import kr.toxicity.advancedskills.equation.EquationLocation
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.World
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Display.Billboard
import org.bukkit.entity.Display.Brightness
import org.bukkit.util.Vector

abstract class DisplaySupplier<T: VirtualDisplay>(
    section: ConfigurationSection,
    private val creator: (World) -> T,
    defaultBillboard: Billboard = Billboard.FIXED
): TrackableSupplier {
    private var apply: (T) -> Unit = {}
    private val duration = section.getLong("duration", 1L).coerceAtLeast(1L)

    companion object {
        private val defaultBrightness = Brightness(15, 15)
        private val defaultScale = Vector(1, 1, 1)
    }

    init {
        val brightness = section.getConfigurationSection("brightness")?.let {
            Brightness(
                it.getInt("block", 15).coerceAtLeast(0).coerceAtMost(15),
                it.getInt("sky", 15).coerceAtLeast(0).coerceAtMost(15)
            )
        } ?: defaultBrightness
        also {
            it.brightness(brightness)
        }
        val scale = section.getConfigurationSection("scale")?.let {
            Vector(
                it.getDouble("x"),
                it.getDouble("y"),
                it.getDouble("z")
            )
        } ?: defaultScale
        also {
            it.scale(scale)
        }
        val billboard = section.getString("billboard")?.let {
            runCatching {
                Billboard.valueOf(it.uppercase())
            }.getOrNull()
        } ?: defaultBillboard
        also {
            it.billboard(billboard)
        }
    }

    private fun also(other: (T) -> Unit) {
        val old = apply
        apply = {
            old(it)
            other(it)
        }
    }

    open fun supply(parent: SkillEntity, count: Int, equationLocation: EquationLocation, tList: T) = parent.addChild(
        AnimatedTrackable(
            duration,
            parent.world(),
            Trackable.of(tList)
        ),
        count,
        equationLocation
    )

    final override fun supply(parent: SkillEntity, scale: Int, equationLocation: EquationLocation): List<SkillEntity> = (0..<scale).map {
        supply(parent, it, equationLocation, creator(parent.world()).apply(apply))
    }
}