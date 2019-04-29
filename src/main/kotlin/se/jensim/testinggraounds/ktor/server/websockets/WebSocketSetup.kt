package se.jensim.testinggraounds.ktor.server.websockets

import io.ktor.application.Application
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.sessions.*
import io.ktor.util.generateNonce
import io.ktor.websocket.WebSockets

fun Application.setupWebSockets() {

    install(Sessions) {
        cookie<AppSession>("SESSION")
    }
    intercept(ApplicationCallPipeline.Features) {
        val s: AppSession? = call.sessions.get()
        if (s == null) {
            val session = AppSession(generateNonce())
            call.sessions.set(session)
        }
    }
    install(WebSockets)
}

data class AppSession(val id: String)
