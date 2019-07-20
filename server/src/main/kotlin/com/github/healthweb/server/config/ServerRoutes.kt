package com.github.healthweb.server.config

import io.ktor.http.content.default
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.http.content.staticRootFolder
import io.ktor.routing.Route
import io.ktor.routing.route
import com.github.healthweb.server.healthcheck.healthcheck
import java.io.File

fun Route.root() {
    route("api") {
        // healthcheck()
    }
    static("/") {
        staticRootFolder = if (PropertiesConfig.isProd()) {
            File(javaClass.getResource("/frontend").toURI())
        } else {
            // Assume dev mode
            File("../frontend/dist")
        }
        files(".")
        default("index.html")
    }
    healthcheck()
}
