package se.jensim.testinggraounds.ktor.server.healthcheck.mock

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route

fun Route.healthcheckMock() {
    route("/mock") {
        get("/") {
            call.respond(HttpStatusCode.InternalServerError, "NOT OK")
        }
    }
}
