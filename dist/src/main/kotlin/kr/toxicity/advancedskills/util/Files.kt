package kr.toxicity.advancedskills.util

import org.bukkit.configuration.ConfigurationSection
import java.io.File

val DATA_FOLDER
    get() = PLUGIN.dataFolder.createFolder()

fun File.createFolder() = apply {
    if (!exists()) mkdir()
}

fun File.subFolder(name: String) = File(this, name).createFolder()

fun File.forEach(block: (File) -> Unit) {
    listFiles()?.forEach(block)
}

fun File.forEachAllFiles(block: (File) -> Unit) {
    if (isDirectory) forEach {
        it.forEachAllFiles(block)
    } else block(this)
}

fun File.forEachAllYaml(block: (File, String, ConfigurationSection) -> Unit) {
    forEachAllFiles {
        if (it.extension != "yml") return@forEachAllFiles
        runCatching {
            it.toYaml().forEachSubConfiguration { s, configurationSection ->
                block(it, s, configurationSection)
            }
        }.onFailure { e ->
            warn("Unable to load this file: ${it.path}")
            warn("Reason: ${e.message}")
        }
    }
}