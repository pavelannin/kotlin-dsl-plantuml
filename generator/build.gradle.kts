plugins {
    kotlin("jvm") version "1.8.22"
}

group = "io.github.pavelannin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(8)
}

dependencies {
    implementation(project(":dsl"))
}