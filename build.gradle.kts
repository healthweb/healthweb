import com.moowork.gradle.node.npm.NpmTask

plugins {
    kotlin("jvm") version "1.3.31"
    id("com.moowork.node") version "1.3.1"
    id("org.sonarqube") version "2.7"
    application
    jacoco
    id("se.jensim.kt2ts") version "0.7.8"
}
repositories {
    mavenCentral()
    maven("https://dl.bintray.com/ylemoigne/maven")

}
group = "se.jensim.testinggrounds"
version = "1.0-SNAPSHOT"

val ktorVersion = "1.1.4"
val jacksonVersion = "2.9.8"
val logbackVersion = "1.2.3"
val slf4jVersion = "1.7.26"
val kmongoVersion = "3.10.1"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.1")

    implementation("io.ktor:ktor-client-apache:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")
    implementation("io.ktor:ktor-websockets:$ktorVersion")

    implementation("org.mongodb:mongo-java-driver:3.10.2")

    implementation("org.litote.kmongo:kmongo-id-jackson:$kmongoVersion")
    implementation("org.litote.kmongo:kmongo-jackson-mapping:$kmongoVersion")
    implementation("org.litote.kmongo:kmongo-coroutine-core:$kmongoVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.slf4j:slf4j-api:$slf4jVersion")

    testImplementation(kotlin("test-junit"))
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
}

kt2ts {
    annotation = "se.jensim.testinggraounds.ktor.server.config.ToTypeScript"
    val a = tasks.compileKotlin.map { it.outputs.files.files }
    classesDirs = files(
        tasks.compileKotlin.map { it.outputs },
        tasks.compileJava.get().outputs
    )
    outputFile = file("$projectDir/src/frontend/src/shared/shared-types.d.ts")
}

application {
    mainClassName = "se.jensim.testinggraounds.ktor.server.Server"
}

node {
    download = false
}

val npmInstall2 = tasks.create("npmInstall2", NpmTask::class) {
    dependsOn(tasks.processResources)
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


tasks.assemble {
    dependsOn(npmInstall2, npmBuild)
}

tasks.findByName("sonarqube")?.apply {
    dependsOn(npmInstall2, npmBuild)
}

tasks.processResources {
    mustRunAfter(npmBuild)
}
