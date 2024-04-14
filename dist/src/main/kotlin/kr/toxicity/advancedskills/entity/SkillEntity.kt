package kr.toxicity.advancedskills.entity

import kr.toxicity.advancedskills.equation.EquationLocation
import kr.toxicity.advancedskills.equation.TEquation
import kr.toxicity.advancedskills.util.*
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.util.Vector
import java.util.*
import kotlin.collections.ArrayList

class SkillEntity(
    private val trackable: AnimatedTrackable,
    private val parent: SkillEntity?,
    private val callback: () -> Unit,
    private val location: () -> Location,
    private val equationLocation: EquationLocation,
    private val move: Boolean,
    private val count: Int,
) {
    companion object {
        private val zeroLocation = TEquation("0", 2).let {
            EquationLocation(
                it,
                it,
                it,
                it,
                it,
                it,
                it,
                it
            )
        }
    }

    private val uuid = UUID.randomUUID()
    private var tick = 0L

    private var remove = false

    constructor(caster: Entity, callback: () -> Unit): this(
        AnimatedTrackable(-1, caster.world, Trackable.of(caster)),
        null,
        callback,
        {
            caster.location
        },
        zeroLocation,
        false,
        0
    )
    private val supplier = ArrayList<EntityLocator>().synchronized()
    private val childes = ArrayList<SkillEntity>().synchronized()
    private val tickTask = ArrayList<(Long) -> Unit>().synchronized()


    private val tracker = run {
        val task = {
            if (parent?.remove == true || trackable.trackable.available) move()
            else removeEntity()
        }
        asyncTaskCount(-1, 1, task, callback)
    }
    init {
        if (trackable.duration > 0) {
            asyncTaskLater(trackable.duration) {
                removeEntity()
            }
        }
    }

    fun cancel() {
        tracker.cancel()
    }
    fun world() = trackable.world

    private fun removeEntity() {
        cancel()
        remove = true
        callback()
        childes.forEachSynchronized { _, t ->
            t.removeEntity()
        }
        trackable.trackable.remove()
        childes.clear()
    }

    fun addChild(child: AnimatedTrackable, count: Int, equationLocation: EquationLocation): SkillEntity {
        val result = SkillEntity(
            child,
            this,
            callback,
            {
                trackable.trackable.location
            },
            equationLocation,
            true,
            count
        )
        childes.add(result)
        return result
    }

    fun addLocator(entityLocator: EntityLocator) {
        supplier.add(entityLocator)
    }
    fun addTickTask(tick: (Long) -> Unit) {
        tickTask.add(tick)
    }

    private fun move() {
        tickTask.forEach {
            it(tick)
        }
        if (move) {
            val vec = Vector()
            var pi = 0.0
            var yw = 0.0
            supplier.forEachSynchronized { remover, it ->
                if (tick < it.delay()) {
                    return@forEachSynchronized
                }
                if (it.duration() > 0 && tick - it.delay() > it.duration()) {
                    remover.remove()
                    return@forEachSynchronized
                }

                val loc = it.locate()

                pi += loc.pitch
                yw += loc.yaw

                vec.add(Vector(loc.x, loc.y, loc.z).rotate(
                    Math.toRadians(loc.rotateYaw),
                    Math.toRadians(loc.rotatePitch),
                    Math.toRadians(loc.rotateRoll)
                ))
            }
            val tr = trackable.trackable
            if (!tr.available) {
                if (tr.async) tr.remove() else task(tr.location) {
                    tr.remove()
                }
            } else {
                val trackableLocation = equationLocation.evaluate(tick.toDouble())
                vec.x += trackableLocation.x
                vec.y += trackableLocation.y
                vec.z += trackableLocation.z
                val loc = location().apply {
                    pitch += pi.toFloat() + trackableLocation.pitch.toFloat()
                    yaw += yw.toFloat() + trackableLocation.yaw.toFloat()
                }
                tr.location = loc.add(
                    vec.rotate(
                        Math.toRadians(trackableLocation.rotateYaw),
                        Math.toRadians(trackableLocation.rotatePitch),
                        Math.toRadians(trackableLocation.rotateRoll)
                    ).rotate(Math.toRadians((loc.yaw + 90).toDouble()), 0.0, 0.0)
                )
            }
        }
        childes.forEachSynchronized { a, t ->
            if (t.remove) {
                a.remove()
                return@forEachSynchronized
            }
            t.move()
        }
        tick++
    }

    fun createTickGetter(): List<() -> Long> {
        val array = ArrayList<() -> Long>()
        array.add {
            count.toLong()
        }
        array.add {
            tick
        }
        var parent = parent
        while (parent is SkillEntity) {
            val old = parent
            array.add {
                old.count.toLong()
            }
            array.add {
                old.tick
            }
            parent = old.parent
        }
        array.reverse()
        return array.subList(2, array.size)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SkillEntity

        return uuid == other.uuid
    }

    override fun hashCode(): Int {
        return uuid?.hashCode() ?: 0
    }

}