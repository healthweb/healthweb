package com.github.healthweb.server.dashboard

import com.github.healthweb.server.extensions.toDto
import com.github.healthweb.server.websockets.WebSocketService
import com.github.healthweb.server.websockets.createBroadcastPath
import com.github.healthweb.shared.Dashboard
import io.ktor.application.call
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.delete
import io.ktor.routing.post
import io.ktor.routing.put
import io.ktor.routing.route
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

@ExperimentalStdlibApi
fun Route.dashboard() {
    val service = DashboardService.singleton
    val ws = WebSocketService.singleton

    route("/dashboard") {
        val log = LoggerFactory.getLogger("route:/dashboard")
        post("/") {
            val dashboard: Dashboard = call.receive(Dashboard::class)
            val saved: Dashboard = service.saveAsync(dashboard).await()
            call.respond(saved)
            ws.broadcast(saved)
        }
        delete("/{id}") {
            val id: Long = call.parameters["id"]!!.toLong()
            val archived = service.archiveAsync(id).await()
            call.respond(OK, "OK")
            ws.broadcast(archived)
        }
        put("/healthcheck/{dashboardId}/{healthCheckId}") {
            val dashboardId: Long = call.parameters["dashboardId"]!!.toLong()
            val healthCheckId: Long = call.parameters["healthCheckId"]!!.toLong()
            val updated = service.addHealthcheckAsync(dashboardId, healthCheckId).await()
            ws.broadcast(updated)
        }
        delete("/healthcheck/{dashboardId}/{healthCheckId}") {
            val dashboardId: Long = call.parameters["dashboardId"]!!.toLong()
            val healthCheckId: Long = call.parameters["healthCheckId"]!!.toLong()
            val updated = service.removeHealthcheckAsync(dashboardId, healthCheckId).await()
            ws.broadcast(updated)
        }
        createBroadcastPath("", Dashboard::class.java) {
            log.info("Pumping initial dashboards")
            transaction { DashboardDao.all().map { it.toDto() } }
        }
    }
}
