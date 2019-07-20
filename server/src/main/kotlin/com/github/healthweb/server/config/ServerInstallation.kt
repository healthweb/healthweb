package com.github.healthweb.server.config

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import io.ktor.util.KtorExperimentalAPI
import com.github.healthweb.server.config.ObjectMapperConfig.config
import com.github.healthweb.server.websockets.setupWebSockets

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
