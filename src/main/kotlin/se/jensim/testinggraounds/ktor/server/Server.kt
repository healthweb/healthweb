package se.jensim.testinggraounds.ktor.server

import io.ktor.application.Application
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import se.jensim.testinggraounds.ktor.server.config.install
import se.jensim.testinggraounds.ktor.server.config.root

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080, module = Application::mainModule).start(wait = true)
}

fun Application.mainModule() {
    install()
    routing {
        route("/") {
            root()
        }
    }
}


