package kr.toxicity.advancedskills.entity

import kr.toxicity.advancedskills.manager.EntityManager
import kr.toxicity.advancedskills.util.forEachSubConfiguration
import kr.toxicity.advancedskills.util.ifNull
import org.bukkit.configuration.ConfigurationSection

class SkillEntitySpawnConfiguration(
    section: ConfigurationSection
) {
    private val name = section.getString("name").ifNull("name value not set.").let {
        EntityManager.config(it).ifNull("this entity doesn't exist: $it")
    }
    private val locations = ArrayList<SkillEntityLocationConfiguration>().apply {
        section.getConfigurationSection("locations")?.forEachSubConfiguration { _, configurationSection ->
            add(SkillEntityLocationConfiguration(configurationSection))
        }
    }

    fun create(entity: SkillEntity): List<SkillEntity> = name.create(entity).onEach { e ->
        val build = e.createTickGetter()
        locations.forEach { loc ->
            val d = loc.build(build.size)
            e.addLocator(object : EntityLocator {
                override fun delay(): Long = loc.delay
                override fun duration(): Long = loc.duration
                override fun locate(): EntityLocation {
                    return d.evaluate(*build.map { i ->
                        i().toDouble()
                    }.toDoubleArray())
                }
            })
        }
    }
}