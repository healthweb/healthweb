package se.jensim.testinggraounds.ktor.server.healthcheck

import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import se.jensim.testinggraounds.ktor.server.healthcheck.mock.healthcheckMock
import se.jensim.testinggraounds.ktor.server.websockets.WebSocketService
import se.jensim.testinggraounds.ktor.server.websockets.createBroadcastPath

fun Route.healthcheck() {
    val service = HealthCheckService.singleton
    service.launchCrawler()
    route("/health") {
        get("/") {
            call.respond("OK")
        }
        post("/") {
            val hc = call.receive(HealthCheckEndpoint::class)
            service.saveNew(hc)
            call.respond(hc)
            WebSocketService.singleton.broadcast(hc)
        }
        healthcheckMock()
        createBroadcastPath("", HealthCheckEndpoint::class.java)
    }
}
