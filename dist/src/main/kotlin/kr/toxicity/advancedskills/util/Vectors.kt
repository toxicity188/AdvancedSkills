package kr.toxicity.advancedskills.util

import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

private val ZERO = Vector()

fun Vector.rotate(xDegree: Double, yDegree: Double, zDegree: Double): Vector {
    if (this == ZERO) return Vector()

    val cx = cos(xDegree)
    val sx = sin(xDegree)

    val cy = cos(yDegree)
    val sy = sin(yDegree)

    val cz = cos(zDegree)
    val sz = sin(zDegree)

    return Vector(
        ((x * cx - z * sx) + (y * sy + x * cy)),
        ((y * cy - x * sy) + (z * sz + y * cz)),
        ((x * sx + z * cx) + (z * cz - y * sz))
    ).normalize().multiply(length())
}