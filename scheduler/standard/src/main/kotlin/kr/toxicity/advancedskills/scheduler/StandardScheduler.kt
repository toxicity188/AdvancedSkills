package kr.toxicity.advancedskills.scheduler

import kr.toxicity.advancedskills.api.AdvancedSkills
import kr.toxicity.advancedskills.api.scheduler.WrappedScheduler
import kr.toxicity.advancedskills.api.scheduler.WrappedTask
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.scheduler.BukkitTask

class StandardScheduler: WrappedScheduler {
    private val plugin
        get() = AdvancedSkills.inst()

    override fun teleport(entity: Entity, location: Location) {
        entity.teleport(location)
    }

    override fun task(runnable: Runnable): WrappedTask = wrap(Bukkit.getScheduler().runTask(plugin, runnable))
    override fun task(location: Location, runnable: Runnable): WrappedTask = task(runnable)
    override fun taskLater(delay: Long, runnable: Runnable): WrappedTask = wrap(Bukkit.getScheduler().runTaskLater(plugin, runnable, delay))
    override fun taskLater(delay: Long, location: Location, runnable: Runnable): WrappedTask = taskLater(delay, runnable)
    override fun taskTimer(delay: Long, period: Long, runnable: Runnable): WrappedTask = wrap(Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period))
    override fun taskTimer(delay: Long, period: Long, location: Location, runnable: Runnable): WrappedTask = taskTimer(delay, period, runnable)

    override fun asyncTask(runnable: Runnable): WrappedTask = wrap(Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable))
    override fun asyncTaskLater(delay: Long, runnable: Runnable): WrappedTask = wrap(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay))
    override fun asyncTaskTimer(delay: Long, period: Long, runnable: Runnable): WrappedTask = wrap(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, period))

    private fun wrap(task: BukkitTask) = object : WrappedTask {
        override fun isCancelled(): Boolean = task.isCancelled
        override fun cancel() {
            task.cancel()
        }
    }
}