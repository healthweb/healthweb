import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.3.31"))
    }
}
plugins {
    kotlin("jvm")
}
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}
group = "se.jensim.testinggrounds"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    testImplementation(kotlin("test"))
    testImplementation("junit:junit:4.12")
    testImplementation("com.github.ntrrgc:ts-generator:1.1.1")
    testImplementation("org.hamcrest:hamcrest:2.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
