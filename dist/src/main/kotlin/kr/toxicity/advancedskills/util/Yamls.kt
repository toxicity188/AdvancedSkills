package kr.toxicity.advancedskills.util

import kr.toxicity.advancedskills.equation.TEquation
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

fun File.toYaml() = YamlConfiguration.loadConfiguration(this)

fun ConfigurationSection.forEachSubConfiguration(block: (String, ConfigurationSection) -> Unit) {
    getKeys(false).forEach { k ->
        getConfigurationSection(k)?.let { c ->
            block(k, c)
        }
    }
}


fun ConfigurationSection.getTEquation(key: String, parameterCount: Int = 1) = getString(key)?.let {
    TEquation(it, parameterCount.coerceAtLeast(1))
} ?: TEquation("0", parameterCount)