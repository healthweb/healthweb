package com.github.healthweb.server.config

import com.github.healthweb.server.healthcheck.healthcheck
import io.ktor.http.content.default
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.http.content.staticRootFolder
import io.ktor.routing.Route
import io.ktor.routing.route
import org.slf4j.LoggerFactory
import java.io.File

fun Route.root() {
    val log = LoggerFactory.getLogger("route:/")
    route("api") {
        // healthcheck()
    }
    static("/") {
        staticRootFolder = if (PropertiesConfig.singleton.isProd) {
            File("/frontend") // Docker assumed path
        } else {
            // Assume dev mode
            File("frontend/dist").also {
                log.info("Serving static files from ${it.absolutePath}")
            }
        }
        files(".")
        default("index.html")
    }
    healthcheck()
}
