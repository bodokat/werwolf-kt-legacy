import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
    application
}

group = "me.binom"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven {
        name = "Kotlin Discord"
        url = uri("https://maven.kotlindiscord.com/repository/maven-public/")
    }

}

dependencies {
    implementation("io.github.cdimascio:dotenv-kotlin:v6.2.2")
    implementation("dev.kord:kord-core:0.7.0-SNAPSHOT")
    implementation("com.kotlindiscord.kord.extensions:kord-extensions:1.4.0-RC6")
    testImplementation(kotlin("test-junit"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "13"
}

application {
    mainClassName = "MainKt"
}