package com.github.healthweb.shared

import me.ntrrgc.tsGenerator.TypeScriptGenerator
import me.ntrrgc.tsGenerator.VoidType.UNDEFINED
import org.hamcrest.CoreMatchers.containsString
import org.junit.Assert.assertThat
import java.io.File
import kotlin.test.Test

class MavenOutPutTest {

    @Test
    fun testOutput() {
        val rootClasses = setOf(
                Dashboard::class,
                HealthCheck::class,
                HealthCheckEndpoint::class,
                HealthCheckError::class,
                HealthChecks::class,
                Link::class,
                ServiceStatus::class
        )
        val ts = TypeScriptGenerator(rootClasses = rootClasses, voidType = UNDEFINED).individualDefinitions
                .joinToString("\n") { "export $it" }
        val out = File("../frontend/src/shared/healthweb-shared.d.ts")

        out.writeText(ts)

        val output = out.readText()
        assertThat(output, containsString("""
            export interface Dashboard {
                archived: boolean;
                description: string | undefined;
                healthchecks: number[];
                id: number | undefined;
                name: string;
            }
        """.trimIndent()))
    }
}
