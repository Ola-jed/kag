plugins {
    kotlin("jvm") version "2.0.20"
    application
}

group = "com.ola"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":lib"))
}

tasks.getByName<JavaExec>("run") {
    standardInput = System.`in`
}

application {
    mainClass = "MainKt"
}

kotlin {
    jvmToolchain(21)
}