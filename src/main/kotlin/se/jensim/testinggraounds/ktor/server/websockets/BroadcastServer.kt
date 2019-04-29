package se.jensim.testinggraounds.ktor.server.websockets


import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.WebSocketSession
import io.ktor.http.cio.websocket.close
import kotlinx.coroutines.channels.ClosedSendChannelException
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Class in charge of the logic of the chat server.
 * It contains handlers to events and commands to send messages to specific users in the server.
 */
class BroadcastServer(private val objectMapper: ObjectMapper) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Associates a session-id to a set of websockets.
     * Since a browser is able to open several tabs and windows with the same cookies and thus the same session.
     * There might be several opened sockets for the same client.
     */
    val members = ConcurrentHashMap<String, MutableList<WebSocketSession>>()

    /**
     * Handles that a member identified with a session id and a socket joined.
     */
    fun memberJoin(member: String, socket: WebSocketSession) {
        val list = members.computeIfAbsent(member) { CopyOnWriteArrayList<WebSocketSession>() }
        list.add(socket)
        log.debug("Connected")
    }

    /**
     * Handles that a [member] with a specific [socket] left the server.
     */
    fun memberLeft(member: String, socket: WebSocketSession) {
        // Removes the socket connection for this member
        val connections = members[member]
        connections?.remove(socket)
        log.debug("Disonnected")
    }

    /**
     * Sends a [message] to all the members in the server, including all the connections per member.
     */
    suspend fun broadcast(data: Any) {
        if (members.isEmpty()) {
            log.debug("Noone to send to =(")
        }
        members.values.forEach { socket ->
            socket.send(Frame.Text(objectMapper.writeValueAsString(data)))
        }
    }

    /**
     * Sends a [message] to a list of [this] [WebSocketSession].
     */
    suspend fun List<WebSocketSession>.send(frame: Frame) {
        forEach {
            try {
                log.info("sending")
                it.send(frame.copy())
            } catch (t: Throwable) {
                try {
                    it.close(CloseReason(CloseReason.Codes.PROTOCOL_ERROR, ""))
                } catch (ignore: ClosedSendChannelException) {
                    // at some point it will get closed
                }
            }
        }
    }
}
