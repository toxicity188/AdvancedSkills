package kr.toxicity.advancedskills.scheduler

import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import kr.toxicity.advancedskills.api.AdvancedSkills
import kr.toxicity.advancedskills.api.scheduler.WrappedScheduler
import kr.toxicity.advancedskills.api.scheduler.WrappedTask
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import java.util.concurrent.TimeUnit

class FoliaScheduler: WrappedScheduler {
    private val plugin
        get() = AdvancedSkills.inst()

    override fun teleport(entity: Entity, location: Location) {
        entity.teleportAsync(location)
    }

    override fun task(runnable: Runnable): WrappedTask = wrap(Bukkit.getGlobalRegionScheduler().run(plugin) {
        runnable.run()
    })
    override fun task(location: Location, runnable: Runnable): WrappedTask = wrap(Bukkit.getRegionScheduler().run(plugin, location) {
        runnable.run()
    })
    override fun taskLater(delay: Long, runnable: Runnable): WrappedTask = wrap(Bukkit.getGlobalRegionScheduler().runDelayed(plugin, {
        runnable.run()
    }, delay))
    override fun taskLater(delay: Long, location: Location, runnable: Runnable): WrappedTask = wrap(Bukkit.getRegionScheduler().runDelayed(plugin, location, {
        runnable.run()
    }, delay))
    override fun taskTimer(delay: Long, period: Long, runnable: Runnable): WrappedTask = wrap(Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, {
        runnable.run()
    }, delay, period))
    override fun taskTimer(delay: Long, period: Long, location: Location, runnable: Runnable): WrappedTask = wrap(Bukkit.getRegionScheduler().runAtFixedRate(plugin, location, {
        runnable.run()
    }, delay, period))

    override fun asyncTask(runnable: Runnable): WrappedTask = wrap(Bukkit.getAsyncScheduler().runNow(plugin) {
        runnable.run()
    })

    override fun asyncTaskLater(delay: Long, runnable: Runnable): WrappedTask = wrap(Bukkit.getAsyncScheduler().runDelayed(plugin, {
        runnable.run()
    }, delay * 50, TimeUnit.MILLISECONDS))

    override fun asyncTaskTimer(delay: Long, period: Long, runnable: Runnable): WrappedTask = wrap(Bukkit.getAsyncScheduler().runAtFixedRate(plugin, {
        runnable.run()
    }, delay * 50, period * 50, TimeUnit.MILLISECONDS))

    private fun wrap(task: ScheduledTask) = object : WrappedTask {
        override fun isCancelled(): Boolean = task.isCancelled
        override fun cancel() {
            task.cancel()
        }
    }
}