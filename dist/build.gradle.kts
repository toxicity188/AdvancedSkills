tasks {
    test {
        useJUnitPlatform()
    }
    jar {
        dependsOn(test)
    }
}

dependencies {
    testImplementation(kotlin("test"))
}