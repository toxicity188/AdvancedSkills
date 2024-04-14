package kr.toxicity.advancedskills.entity

import kr.toxicity.advancedskills.equation.EquationLocation
import org.bukkit.configuration.ConfigurationSection

class EmptySupplier(section: ConfigurationSection): TrackableSupplier {

    private val duration = section.getLong("duration", 1L).coerceAtLeast(1L)

    override fun supply(parent: SkillEntity, scale: Int, equationLocation: EquationLocation): List<SkillEntity> {
        return (0..<scale).map {
            parent.addChild(
                AnimatedTrackable(
                    duration,
                    parent.world(),
                    Trackable.empty(parent.world())
                ),
                it,
                equationLocation
            )
        }
    }
}