import com.moowork.gradle.node.npm.NpmTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.31"
    id("com.moowork.node") version "1.3.1"
    id("org.sonarqube") version "2.7"
    application
}

val ktorVersion = "1.1.4"
val jacksonVersion = "2.9.8"
val logbackVersion = "1.2.3"
val slf4jVersion = "1.7.26"

group = "se.jensim.testinggrounds"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")
    implementation("io.ktor:ktor-websockets:$ktorVersion")

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.slf4j:slf4j-api:$slf4jVersion")

    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
}

application {
    mainClassName = "se.jensim.testinggraounds.ktor.server.Server"
}

node {
    download = false
}

val npmInstall2 = tasks.create("npmInstall2", NpmTask::class) {
    group = "node"
    description = "Install packages from package.json"
    setWorkingDir(file("${project.projectDir}/src/frontend"))
    setArgs(listOf("install"))
    inputs.file("${project.projectDir}/src/frontend/package-lock.json")
    outputs.dir("${project.projectDir}/src/frontend/node_modules")
}

val npmBuild = tasks.create("npmBuild", NpmTask::class) {
    group = "node"
    description = "Build production release of the Frontend resources"
    setWorkingDir(file("${project.projectDir}/src/frontend"))
    setArgs(listOf("run", "build"))
    mustRunAfter(npmInstall2)
    inputs.dir("${project.projectDir}/src/frontend/")
    outputs.dir("${project.projectDir}/src/main/resources/frontend/")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.assemble {
    dependsOn(npmInstall2, npmBuild)
}

tasks.findByName("sonarqube")?.apply {
    dependsOn(npmInstall2, npmBuild)
}

tasks.processResources {
    mustRunAfter(npmBuild)
}
