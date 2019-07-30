package com.github.healthweb.server.websockets

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.healthweb.server.config.ObjectMapperConfig
import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.Frame.Text
import io.ktor.http.cio.websocket.close
import io.ktor.routing.Route
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.websocket.webSocket
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

open class WebSocketService(private val objectMapper: ObjectMapper) {

    companion object {
        val singleton by lazy {
            WebSocketService(ObjectMapperConfig.config())
        }
    }

    private val log = LoggerFactory.getLogger(javaClass)
    private val allroutes = ConcurrentHashMap<Class<out Any>, BroadcastServer>()

    fun <T : Any> getServer(type: Class<T>): BroadcastServer {
        if (allroutes[type] != null) {
            log.error("Trying to add the same type [$type] once again! Skipping.")
        }
        return allroutes.computeIfAbsent(type) { BroadcastServer(objectMapper) }
    }

    open suspend fun <T : Any> broadcast(data: T) {
        allroutes[data::class.java]
            ?.broadcast(data)
            ?: log.error("Unable to broadcast, no BroadcastServer for type ${data::class.java}")
    }
}

@ExperimentalStdlibApi
fun <T : Any> Route.createBroadcastPath(path: String, type: Class<T>) {
    val singleton = WebSocketService.singleton
    val server = singleton.getServer(type)
    val log = LoggerFactory.getLogger("ws:/$path")

    webSocket(path) {
        val session: AppSession? = call.sessions.get()
        if (session == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session"))
            return@webSocket
        }
        try {
            server.memberJoin(session.id, this)
            for (frame in incoming) {
                if(frame is Text){
                    log.warn("Got something I should not [TEXT: \"${frame.data.decodeToString()}\"]")
                }else{
                    log.warn("Got something I should not [$frame]")
                }
            }
        } catch (e: Exception) {
            log.error("Horrible exception in websocket connection", e)
        } finally {
            server.memberLeft(session.id, this)
        }
    }
}
