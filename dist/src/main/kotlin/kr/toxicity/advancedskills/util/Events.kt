package kr.toxicity.advancedskills.util

import org.bukkit.Bukkit
import org.bukkit.event.Event

fun Event.call() = Bukkit.getPluginManager().callEvent(this)