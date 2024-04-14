package kr.toxicity.advancedskills.manager

import kr.toxicity.advancedskills.chunk.ChunkLoc
import kr.toxicity.advancedskills.entity.SkillEntity
import kr.toxicity.advancedskills.entity.SkillEntityConfiguration
import kr.toxicity.advancedskills.pack.PackData
import kr.toxicity.advancedskills.util.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.world.ChunkUnloadEvent
import java.util.UUID

object EntityManager: SkillsManager {

    private val entityMap = HashMap<UUID, SkillEntity>()
    private val chunkMap = HashMap<ChunkLoc, SkillEntity>()

    private val configMap = HashMap<String, SkillEntityConfiguration>()

    override fun start() {
        Bukkit.getPluginManager().registerEvents(object : Listener {
            @EventHandler
            fun quit(e: PlayerQuitEvent) {
                entityMap.remove(e.player.uniqueId)?.cancel()
            }
            @EventHandler
            fun unload(e: ChunkUnloadEvent) {
                chunkMap.remove(ChunkLoc(e.chunk.x, e.chunk.z))?.cancel()
            }
        }, PLUGIN)
    }

    override fun reload(data: PackData) {
        configMap.clear()
        data.dataFolder.subFolder("entities").forEachAllYaml { file, s, configurationSection ->
            runCatching {
                configMap[s] = SkillEntityConfiguration(configurationSection)
            }.onFailure { e ->
                warn("Unable to load config: $s in ${file.path}")
                warn("Reason: ${e.message}")
            }
        }
        configMap.entries.removeIf {
            runCatching {
                it.value.acceptLazyTask()
                false
            }.getOrElse { e ->
                warn("Unable to finalize this config: ${it.key}")
                warn("Reason: ${e.message}")
                true
            }
        }
    }
    fun config(name: String) = configMap[name]
    fun entity(entity: Entity) = entityMap.computeIfAbsent(entity.uniqueId) {
        SkillEntity(entity) {
            entityMap.remove(it)
        }
    }
    fun location(world: World, location: Location): SkillEntity {
        val chunk = location.chunk
        return chunkMap.computeIfAbsent(ChunkLoc(chunk.x, chunk.z)) {
            SkillEntity(world, location) {
                chunkMap.remove(it)
            }
        }
    }
}