package se.jensim.testinggraounds.ktor.server.websockets

import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.close
import io.ktor.routing.Route
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.websocket.webSocket
import org.slf4j.LoggerFactory
import se.jensim.testinggraounds.ktor.server.config.ObjectMapperConfig.config
import java.util.concurrent.ConcurrentHashMap

object WebSocketRoute {

    private val log = LoggerFactory.getLogger(javaClass)
    private val objectMapper = config()
    private val allroutes = ConcurrentHashMap<Class<out Any>, BroadcastServer>()

    suspend fun <T : Any> broadcast(data: T) {
        allroutes[data::class.java]
            ?.broadcast(data)
            ?: throw RuntimeException("Unable to broadcast, no Broadcastserver for type ${data::class.java}")
    }

    fun <T : Any> Route.createBroadcastPath(path: String, type: Class<T>) {
        if (allroutes[type] != null) {
            log.error("Trying to add the same type [$type] once again! Skipping.")
            return
        } else {
            log.debug("Adding websocket broadcast feature to route $this/$path of type $type")
        }
        val server = allroutes.computeIfAbsent(type) { BroadcastServer(objectMapper) }

        webSocket(path) {
            val session: AppSession? = call.sessions.get()
            if (session == null) {
                close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session"))
                return@webSocket
            }
            try {
                server.memberJoin(session.id, this)
                for (frame in incoming) {
                    log.warn("Got something I should not [$frame]")
                }
            } catch (e: Exception) {
                log.error("Horrible exception in websocket connection", e)
            } finally {
                server.memberLeft(session.id, this)
            }
        }
    }
}
