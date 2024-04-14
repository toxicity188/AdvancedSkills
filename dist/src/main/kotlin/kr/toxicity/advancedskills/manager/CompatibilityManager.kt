package kr.toxicity.advancedskills.manager

import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent
import kr.toxicity.advancedskills.compatibility.EffectEntityMechanic
import kr.toxicity.advancedskills.pack.PackData
import kr.toxicity.advancedskills.util.PLUGIN
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object CompatibilityManager: SkillsManager {

    override fun start() {
        if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) Bukkit.getPluginManager().registerEvents(object : Listener {
            @EventHandler
            fun reload(e: MythicMechanicLoadEvent) {
                when (e.mechanicName.lowercase()) {
                    "effectentity" -> e.register(EffectEntityMechanic(e.config))
                }
            }
        }, PLUGIN)
    }

    override fun reload(data: PackData) {
    }
}