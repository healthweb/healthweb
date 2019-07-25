package com.github.healthweb.server.healthcheck.mock

import com.github.healthweb.shared.DropwizardHealthCheck
import com.github.healthweb.shared.HealthCheck
import com.github.healthweb.shared.HealthCheckError
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route

fun Route.healthcheckMock() {
    route("/mock") {
        get("/unhealthy") {
            call.respond(HttpStatusCode.InternalServerError, unhealthy)
        }
        get("/healthy") {
            call.respond(healthy)
        }
    }
}

private val healthy: DropwizardHealthCheck = mapOf(
        "Database" to HealthCheck("All ok", true),
        "Filesystem" to HealthCheck("9% full disk", true),
        "Ethernet" to HealthCheck("eth0 is feeling great", true)
)

private val unhealthy: DropwizardHealthCheck = mapOf(
        "Database" to HealthCheck("All ok", true),
        "Filesystem" to HealthCheck("99% full disk", false),
        "Ethernet" to HealthCheck(
                "Unable to connect to interface",
                false,
                HealthCheckError(
                        "Unable to connect to interface",
                        listOf("boo", "baaa", "looo")
                )
        )
)
