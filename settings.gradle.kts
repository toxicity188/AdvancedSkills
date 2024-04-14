plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "AdvancedSkills"

include(
    "api",
    "dist",
    "scheduler:standard",
    "scheduler:folia",
    "nms:v1_19_R3",
    "nms:v1_20_R1",
    "nms:v1_20_R2",
    "nms:v1_20_R3"
)