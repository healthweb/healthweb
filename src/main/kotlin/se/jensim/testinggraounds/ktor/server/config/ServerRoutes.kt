package se.jensim.testinggraounds.ktor.server.config

import io.ktor.http.content.default
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.http.content.staticRootFolder
import io.ktor.routing.Route
import io.ktor.routing.Routing
import io.ktor.routing.route
import org.slf4j.LoggerFactory
import se.jensim.testinggraounds.ktor.server.healthcheck.healthcheck
import java.io.File

fun Route.root() {
    val log = LoggerFactory.getLogger(Routing::class.java)
    route("api") {
        healthcheck()
    }
    static("/") {
        staticRootFolder = File(javaClass.getResource("/static").toURI())
        /*
         TODO
         Make able to handle env dev for serving from file system, and not classpath
         println("staticRootFolder=$staticRootFolder")
         */
        files(".")
        default("index.html")
    }
    healthcheck()
}
