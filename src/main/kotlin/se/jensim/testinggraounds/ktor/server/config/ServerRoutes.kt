package se.jensim.testinggraounds.ktor.server.config

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route
import org.slf4j.LoggerFactory
import se.jensim.testinggraounds.ktor.server.healthcheck.Healthcheck
import se.jensim.testinggraounds.ktor.server.healthcheck.healthcheck

fun Route.root() {
    val log = LoggerFactory.getLogger(Routing::class.java)
    get("/") {
        log.info("Responding..!!")
        call.respond(HttpStatusCode.OK, Healthcheck("HEJ"))
    }
    route("api") {
        healthcheck()
    }
    healthcheck()
}
