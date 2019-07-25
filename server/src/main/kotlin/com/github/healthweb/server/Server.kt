@file:JvmName("Server")

package com.github.healthweb.server

import com.github.healthweb.server.config.DatabaseConfig
import com.github.healthweb.server.config.install
import com.github.healthweb.server.config.root
import com.typesafe.config.ConfigFactory
import io.ktor.application.Application
import io.ktor.config.HoconApplicationConfig
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {

    val conf = HoconApplicationConfig(ConfigFactory.load())
    val port = conf.config("ktor.deployment").property("port").getString().toInt()

    LoggerFactory.getLogger("Server").info("Starting server on port $port")
    embeddedServer(Netty, port = port, module = Application::mainModule).start(wait = true)
}

fun Application.mainModule() {
    DatabaseConfig.init()
    install()
    routing {
        route("/") {
            root()
        }
    }
}
