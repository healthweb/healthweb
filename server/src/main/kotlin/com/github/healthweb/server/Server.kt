@file:JvmName("Server")

package com.github.healthweb.server

import com.github.healthweb.server.config.DatabaseConfig
import com.github.healthweb.server.config.PropertiesConfig
import com.github.healthweb.server.config.install
import com.github.healthweb.server.config.root
import io.ktor.application.Application
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {

    val conf = PropertiesConfig.singleton
    LoggerFactory.getLogger("Server").info("Starting server on port ${conf.port}")
    embeddedServer(Netty, port = conf.port,
            module = Application::mainModule).start(wait = true)
}

fun Application.mainModule() {
    DatabaseConfig.singleton.init()
    install()
    routing {
        route("/") {
            root()
        }
    }
}
