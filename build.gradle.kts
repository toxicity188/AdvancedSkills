plugins {
    `java-library`
    kotlin("jvm") version "1.9.23"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.papermc.paperweight.userdev") version "1.5.12" apply false
    id("org.jetbrains.dokka") version "1.9.20"
}

val api = project(":api")
val dist = project(":dist")
val scheduler = project(":scheduler")
val nms = project(":nms")

val latest = "1.20.4"
val adventure = "4.16.0"
val platform = "4.3.2"

val nmsVersions = listOf(
    "v1_19_R3",
    "v1_20_R1",
    "v1_20_R2",
    "v1_20_R3"
)

val creative = "1.7.0"

val unnamedApi = listOf(
    "team.unnamed:creative-api:$creative",
    "team.unnamed:creative-serializer-minecraft:$creative",
    "team.unnamed:creative-server:$creative"
)

allprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")

    group = "kr.toxicity.advancedskills"
    version = "1.0"

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://mvn.lumine.io/repository/maven-public/")
    }

    tasks {
        compileJava {
            options.encoding = Charsets.UTF_8.name()
        }
        javadoc {
            options.encoding = Charsets.UTF_8.name()
        }
    }
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")

    dependencies {
        compileOnly("net.objecthunter:exp4j:0.4.8")
        compileOnly("io.lumine:Mythic-Dist:5.6.1")
        compileOnly("com.ticxo.modelengine:ModelEngine:R4.0.4")

        unnamedApi.forEach {
            compileOnly(it)
        }
    }
}

listOf(
    api,
    dist
).forEach {
    it.dependencies {
        compileOnly("org.spigotmc:spigot-api:$latest-R0.1-SNAPSHOT")
        compileOnly("net.kyori:adventure-api:$adventure")
        compileOnly("net.kyori:adventure-platform-bukkit:$platform")
    }
}

dist.dependencies {
    compileOnly(api)
    scheduler.subprojects.forEach {
        compileOnly(it)
    }
    nmsVersions.forEach {
        compileOnly(project(":nms:$it"))
    }
}
dist.tasks {
    processResources {
        filteringCharset = Charsets.UTF_8.name()
        val props = mapOf(
            "version" to project.version,
            "platform" to platform,
            "creative" to creative,
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}

nms.subprojects {
    apply(plugin = "io.papermc.paperweight.userdev")
    dependencies {
        compileOnly(api)
    }
}

scheduler.subprojects {
    dependencies {
        compileOnly(api)
    }
}

scheduler.project("standard") {
    dependencies {
        compileOnly("org.spigotmc:spigot-api:$latest-R0.1-SNAPSHOT")
    }
}

scheduler.project("folia") {
    dependencies {
        compileOnly("dev.folia:folia-api:$latest-R0.1-SNAPSHOT")
    }
}

dependencies {
    implementation(api)
    implementation(dist)
    scheduler.subprojects.forEach {
        implementation(it)
    }
    nmsVersions.forEach {
        implementation(project(":nms:${it}", configuration = "reobf"))
    }
}

val sourceJar by tasks.creating(Jar::class.java) {
    dependsOn(tasks.classes)
    fun getProjectSource(project: Project): Array<File> {
        return if (project.subprojects.isEmpty()) project.sourceSets.main.get().allSource.srcDirs.toTypedArray() else ArrayList<File>().apply {
            project.subprojects.forEach {
                addAll(getProjectSource(it))
            }
        }.toTypedArray()
    }
    archiveClassifier = "source"
    from(*getProjectSource(project))
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
val dokkaJar by tasks.creating(Jar::class.java) {
    dependsOn(tasks.dokkaHtmlMultiModule)
    archiveClassifier = "dokka"
    from(layout.buildDirectory.dir("dokka/htmlMultiModule").orNull?.asFile)
}

tasks {
    jar {
        dependsOn(clean)
        finalizedBy(shadowJar)
    }
    shadowJar {
        nmsVersions.forEach {
            dependsOn(":nms:${it}:reobfJar")
        }
        archiveClassifier = ""
        dependencies {
            exclude(dependency("org.jetbrains:annotations:13.0"))
        }
        fun prefix(pattern: String) {
            relocate(pattern, "${project.group}.shaded.$pattern")
        }
        prefix("kotlin")
        finalizedBy(sourceJar)
        finalizedBy(dokkaJar)
    }
}

val targetJavaVersion = 17

java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
}

kotlin {
    jvmToolchain(targetJavaVersion)
}