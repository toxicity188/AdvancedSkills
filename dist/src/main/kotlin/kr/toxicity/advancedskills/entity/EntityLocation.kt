package kr.toxicity.advancedskills.entity

import org.bukkit.Location

data class EntityLocation(
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Double,
    val pitch: Double,


    val rotateYaw: Double,
    val rotatePitch: Double,
    val rotateRoll: Double
) {
    companion object {
        fun fromLocation(location: Location) = EntityLocation(
            location.x,
            location.y,
            location.z,
            location.yaw.toDouble(),
            location.pitch.toDouble(),

            0.0,
            0.0,
            0.0
        )
    }

    operator fun plus(other: EntityLocation) = EntityLocation(
        x + other.x,
        y + other.y,
        z + other.z,
        yaw + other.yaw,
        pitch + other.pitch,

        rotateYaw + other.rotateYaw,
        rotatePitch + other.rotatePitch,
        rotateRoll + other.rotateRoll
    )
}