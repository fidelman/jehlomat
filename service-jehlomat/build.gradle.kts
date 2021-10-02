val ktorVersion: String by project
val ktormVersion: String = "3.4.1"

plugins {
    application
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.github.johnrengelman.shadow")
}

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

dependencies {
    implementation(kotlin("stdlib"))

    // Ktor
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")
    implementation("io.ktor:ktor-html-builder:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")

    // Jackson serializer modules
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-joda:2.10.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.2")

    implementation("ch.qos.logback:logback-classic:1.2.3")

    // Koin DI
    implementation("org.koin:koin-ktor:2.0.1")

    // Http client
    implementation("com.github.kittinunf.fuel:fuel:2.2.1")

    // Ktorm
    implementation("org.ktorm:ktorm-core:${ktormVersion}")
    implementation("org.ktorm:ktorm-support-postgresql:${ktormVersion}")
    implementation("postgresql:postgresql:9.0-801.jdbc4")

    // Config
    implementation("com.typesafe:config:1.4.0")
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveBaseName.set(project.name)
    archiveVersion.set("")
    archiveAppendix.set("")
}