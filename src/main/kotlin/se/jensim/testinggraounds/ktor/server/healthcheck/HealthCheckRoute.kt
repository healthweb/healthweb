package se.jensim.testinggraounds.ktor.server.healthcheck

import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import se.jensim.testinggraounds.ktor.server.healthcheck.mock.healthcheckMock
import se.jensim.testinggraounds.ktor.server.websockets.WebSocketRoute.broadcast
import se.jensim.testinggraounds.ktor.server.websockets.WebSocketRoute.createBroadcastPath

fun Route.healthcheck() {
    route("/health") {
        get("/") {
            call.respond("OK")
        }
        post("/") {
            val hc = call.receive(HealthcheckEndpoint::class)
            val resp = HealthCheckCrawlerService.service.crawl(hc)
            call.respond(resp)
            broadcast(resp)
        }
        healthcheckMock()
        createBroadcastPath("", HealthcheckEndpoint::class.java)
    }
}
