package kr.toxicity.advancedskills.entity

import kr.toxicity.advancedskills.equation.EquationLocation
import kr.toxicity.advancedskills.equation.TEquation
import kr.toxicity.advancedskills.util.forEachSubConfiguration
import kr.toxicity.advancedskills.util.ifNull
import org.bukkit.configuration.ConfigurationSection

class SkillEntityConfiguration(section: ConfigurationSection) {
    companion object {
        private val creator: Map<String, (ConfigurationSection) -> TrackableSupplier> = mapOf(
            "model" to {
                ModelSupplier(it)
            },
            "text" to {
                TextDisplaySupplier(it)
            },
            "item" to {
                ItemDisplaySupplier(it)
            },
            "block" to {
                BlockDisplaySupplier(it)
            },
            "empty" to {
                EmptySupplier(it)
            }
        )
    }

    private val supplier = section.getString("type").ifNull("type value not set.").let {
        creator[it].ifNull("this type doesn't exist: $it")(section)
    }
    private val lazyTask = ArrayList<() -> Unit>()
    private val child = ArrayList<SkillEntitySpawnConfiguration>()
    private val count = section.getInt("count").coerceAtLeast(1)

    private val equation = section.getConfigurationSection("equation")?.let {
        EquationLocation(it)
    } ?: TEquation("0").let {
        EquationLocation(it, it, it, it, it, it, it, it)
    }

    private val locations = ArrayList<SkillEntityLocationConfiguration>().apply {
        section.getConfigurationSection("locations")?.forEachSubConfiguration { _, configurationSection ->
            add(SkillEntityLocationConfiguration(configurationSection))
        }
    }

    init {
        section.getConfigurationSection("childes")?.let { childes ->
            lazyTask.add {
                childes.forEachSubConfiguration { _, config ->
                    child.add(SkillEntitySpawnConfiguration(config))
                }
            }
        }
    }

    fun acceptLazyTask() {
        lazyTask.forEach {
            it()
        }
        lazyTask.clear()
    }

    fun create(parent: SkillEntity): List<SkillEntity> {
        val entity = supplier.supply(parent, count, equation)

        fun addLocations(target: SkillEntity, list: List<SkillEntityLocationConfiguration>) {
            if (list.isEmpty()) return
            val getter = target.createTickGetter()
            list.forEach {
                val create = it.build(getter.size)
                target.addLocator(object : EntityLocator {
                    override fun delay(): Long = it.delay
                    override fun duration(): Long = it.duration
                    override fun locate(): EntityLocation = create.evaluate(*getter.map {
                        it().toDouble()
                    }.toDoubleArray())
                })
            }
        }
        entity.forEach { e ->
            addLocations(e, locations)
            child.forEach { ce ->
                ce.create(e)
            }
        }
        return entity
    }
}