package se.jensim.testinggraounds.ktor.server.healthcheck.mock

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import se.jensim.testinggraounds.ktor.server.healthcheck.DropwizardHealthCheck
import se.jensim.testinggraounds.ktor.server.healthcheck.HealthCheck
import se.jensim.testinggraounds.ktor.server.healthcheck.HealthCheckError

fun Route.healthcheckMock() {
    route("/mock") {
        get("/") {
            call.respond(HttpStatusCode.InternalServerError, m)
        }
    }
}

private val m: DropwizardHealthCheck = mapOf(
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
