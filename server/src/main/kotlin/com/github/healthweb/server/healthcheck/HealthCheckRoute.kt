package com.github.healthweb.server.healthcheck

import com.github.healthweb.server.healthcheck.mock.healthcheckMock
import com.github.healthweb.server.websockets.WebSocketService
import com.github.healthweb.server.websockets.createBroadcastPath
import com.github.healthweb.shared.HealthCheckEndpoint
import com.github.healthweb.shared.TagMarker
import io.ktor.application.call
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route

@ExperimentalStdlibApi
fun Route.healthcheck() {
    val service = HealthCheckService.singleton
    service.launchCrawler()
    route("/health") {
        get("/") {
            val all = service.getAllAsync().await()
            call.respond(all)
        }
        post("/") {
            val hc = call.receive(HealthCheckEndpoint::class)
            val saved: HealthCheckEndpoint = service.saveNew(hc)
            call.respond(saved)
            WebSocketService.singleton.broadcast(saved)
        }
        post("/tag"){
            val marker = call.receive(TagMarker::class)
            val hc = service.addTag(marker.id, marker.tag)
            call.respond(OK)
            WebSocketService.singleton.broadcast(hc)
        }
        post("/untag"){
            val marker = call.receive(TagMarker::class)
            val hc = service.removeTag(marker.id, marker.tag)
            call.respond(OK)
            WebSocketService.singleton.broadcast(hc)
        }
        healthcheckMock()
        createBroadcastPath("", HealthCheckEndpoint::class.java)
    }
}
