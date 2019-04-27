@file:JvmName("Server")

package se.jensim.testinggraounds.ktor.server

import com.typesafe.config.ConfigFactory
import io.ktor.application.Application
import io.ktor.config.HoconApplicationConfig
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.KtorExperimentalAPI
import se.jensim.testinggraounds.ktor.server.config.install
import se.jensim.testinggraounds.ktor.server.config.root

@KtorExperimentalAPI
fun main(args: Array<String>) {
    val conf = HoconApplicationConfig(ConfigFactory.load())
    val port = conf.config("ktor.deployment").property("port").getString().toInt()
    embeddedServer(Netty, port = port, module = Application::mainModule).start(wait = true)
}

@KtorExperimentalAPI
fun Application.mainModule() {
    install()
    routing {
        route("/") {
            root()
        }
    }
}


