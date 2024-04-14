package kr.toxicity.advancedskills.util

import kr.toxicity.advancedskills.api.scheduler.WrappedTask
import org.bukkit.Location

fun taskCount(location: Location, count: Long, period: Long, block: () -> Unit, callback: () -> Unit): WrappedTask {
    return CountableTask(location, count, period, block, callback).task
}
fun asyncTaskCount(count: Long, period: Long, block: () -> Unit, callback: () -> Unit): WrappedTask {
    return CountableTask(null, count, period, block, callback).task
}

private class CountableTask(location: Location?, count: Long, period: Long, block: () -> Unit, callback: () -> Unit) {
    private var i = 0L
    val task = run {
        val task = {
            if (count <= 0 || i++ < count) block()
            else {
                cancel()
                callback()
            }
        }
        location?.let {
            taskTimer(it, 1, period, task)
        } ?: asyncTaskTimer(1, period, task)
    }
    private fun cancel() {
        task.cancel()
    }
}