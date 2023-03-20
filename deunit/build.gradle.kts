plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "com.amazon.data.engineer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.postgresql:postgresql:42.6.0")
    implementation(kotlin("test")) // assert* is needed in main
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}