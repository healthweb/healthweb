package se.jensim.testinggraounds.ktor.server.config

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import se.jensim.testinggraounds.ktor.server.config.ObjectMapperConfig.config
import se.jensim.testinggraounds.ktor.server.websockets.setupWebSockets

fun Application.install() {
    install(DefaultHeaders)
    install(Compression)
    install(CallLogging)
    install(ContentNegotiation) {
        jackson {
            config(this)
        }
    }
    setupWebSockets()
}
