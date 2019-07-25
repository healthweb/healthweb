package com.github.healthweb.server.healthcheck

import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import com.github.healthweb.server.healthcheck.mock.healthcheckMock
import com.github.healthweb.server.websockets.WebSocketService
import com.github.healthweb.server.websockets.createBroadcastPath
import com.github.healthweb.shared.HealthCheckEndpoint

fun Route.healthcheck() {
    val service = HealthCheckService.singleton
    service.launchCrawler()
    route("/health") {
        get("/") {
            call.respond("OK")
        }
        post("/") {
            val hc = call.receive(HealthCheckEndpoint::class)
            val saved: HealthCheckEndpoint = service.saveNew(hc)
            call.respond(saved)
            WebSocketService.singleton.broadcast(saved)
        }
        healthcheckMock()
        createBroadcastPath("", HealthCheckEndpoint::class.java)
    }
}
