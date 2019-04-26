package se.jensim.testinggraounds.ktor.server.healthcheck

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import se.jensim.testinggraounds.ktor.server.healthcheck.mock.healthcheckMock

fun Route.healthcheck() {
    route("/health") {
        get("/") {
            call.respond("OK")
        }
        healthcheckMock()
    }
}
