package kr.toxicity.advancedskills.util

import kr.toxicity.advancedskills.api.AdvancedSkills
import org.bukkit.Location

val PLUGIN
    get() = AdvancedSkills.inst()

fun info(message: String) = PLUGIN.logger.info(message)
fun warn(message: String) = PLUGIN.logger.warning(message)

fun task(block: () -> Unit) = PLUGIN.scheduler().task(block)
fun task(location: Location, block: () -> Unit) = PLUGIN.scheduler().task(location, block)

fun taskLater(delay: Long, block: () -> Unit) = PLUGIN.scheduler().taskLater(delay, block)
fun taskLater(location: Location, delay: Long, block: () -> Unit) = PLUGIN.scheduler().taskLater(delay, location, block)

fun taskTimer(delay: Long, period: Long, block: () -> Unit) = PLUGIN.scheduler().taskTimer(delay, period, block)
fun taskTimer(location: Location, delay: Long, period: Long, block: () -> Unit) = PLUGIN.scheduler().taskTimer(delay, period, location, block)

fun asyncTask(block: () -> Unit) = PLUGIN.scheduler().asyncTask(block)
fun asyncTaskLater(delay: Long, block: () -> Unit) = PLUGIN.scheduler().asyncTaskLater(delay, block)
fun asyncTaskTimer(delay: Long, period: Long, block: () -> Unit) = PLUGIN.scheduler().asyncTaskTimer(delay, period, block)