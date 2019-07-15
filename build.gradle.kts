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
group = "com.github.healthweb"
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
    dependsOn(tasks.kt2ts)
    description = "Install packages from package.json"
    setArgs(listOf("install"))
    inputs.file("${project.projectDir}/src/frontend/package-lock.json")
    outputs.dir("${project.projectDir}/src/frontend/node_modules")
}

val npmBuild = tasks.create("npmBuild", NpmTask::class) {
    dependsOn(npmInstall2)
    description = "Build production release of the Frontend resources"
    setArgs(listOf("run", "build"))
    val baseDir = "${project.projectDir}/src/frontend/"
    inputs.files(fileTree("$baseDir/src"), fileTree("$baseDir/e2e"), "$baseDir/package.json", "$baseDir/angular.json")
    outputs.dir("${project.projectDir}/src/main/resources/frontend/")
}

val npmTest: NpmTask = tasks.create("npmTest", NpmTask::class) {
    dependsOn(npmInstall2)
    group = "node"
    description = "Test of the Frontend resources"
    setArgs(listOf("run", "test"))
    val baseDir = "${project.projectDir}/src/frontend"
    inputs.files(fileTree("$baseDir/src"), fileTree("$baseDir/e2e"), "$baseDir/package.json", "$baseDir/angular.json")
    outputs.dir("${project.projectDir}/src/main/resources/frontend/")
}

val fatJar = task("fatJar", type = Jar::class) {
    archiveBaseName.set("${project.name}-fat")
    manifest {
        attributes["Implementation-Title"] = "Jar File"
        attributes["Implementation-Version"] = version
        attributes["Main-Class"] = "se.jensim.testinggraounds.ktor.server.Server"
    }
    from(configurations.runtime.map { it.files.map { if (it.isDirectory) it else zipTree(it) } })
    with(tasks["jar"] as CopySpec)
}

tasks {
    build {
        dependsOn(fatJar)
    }
    assemble {
        dependsOn(npmInstall2, npmBuild)
    }
    "sonarqube" {
        dependsOn(npmInstall2, npmBuild)
    }
    processResources {
        mustRunAfter(npmBuild)
    }
    test {
        dependsOn(npmTest)
    }

    withType(NpmTask::class) {
        val baseDir = "${project.projectDir}/src/frontend/"
        group = "node"
        setWorkingDir(file("${project.projectDir}/src/frontend/"))
    }
}
