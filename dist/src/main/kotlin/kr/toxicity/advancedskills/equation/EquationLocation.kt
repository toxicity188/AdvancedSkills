package kr.toxicity.advancedskills.equation

import kr.toxicity.advancedskills.entity.EntityLocation
import kr.toxicity.advancedskills.util.getTEquation
import org.bukkit.configuration.ConfigurationSection

class EquationLocation(
    private val x: TEquation,
    private val y: TEquation,
    private val z: TEquation,
    private val pitch: TEquation,
    private val yaw: TEquation,

    private val rotateYaw: TEquation,
    private val rotatePitch: TEquation,
    private val rotateRoll: TEquation,
) {
    constructor(section: ConfigurationSection, parameterCount: Int = 1): this(
        section.getTEquation("x", parameterCount),
        section.getTEquation("y", parameterCount),
        section.getTEquation("z", parameterCount),
        section.getTEquation("pitch", parameterCount),
        section.getTEquation("yaw", parameterCount),
        section.getTEquation("rotate-yaw", parameterCount),
        section.getTEquation("rotate-pitch", parameterCount),
        section.getTEquation("rotate-roll", parameterCount),
    )
    fun evaluate(vararg double: Double) = EntityLocation(
        x.evaluate(*double),
        y.evaluate(*double),
        z.evaluate(*double),
        pitch.evaluate(*double),
        yaw.evaluate(*double),
        rotateYaw.evaluate(*double),
        rotatePitch.evaluate(*double),
        rotateRoll.evaluate(*double)
    )
}