package kr.toxicity.advancedskills.entity

import kr.toxicity.advancedskills.equation.EquationLocation
import kr.toxicity.advancedskills.util.ifNull
import org.bukkit.configuration.ConfigurationSection

class SkillEntityLocationConfiguration(section: ConfigurationSection) {
    val duration = section.getLong("duration")
    val delay = section.getLong("delay").coerceAtLeast(0)
    private val equation = section.getConfigurationSection("equation").ifNull("equation value not found.")

    fun build(parameterCount: Int = 1) = EquationLocation(equation, parameterCount)
}