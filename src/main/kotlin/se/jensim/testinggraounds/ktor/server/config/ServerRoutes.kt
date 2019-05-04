package se.jensim.testinggraounds.ktor.server.config

import com.typesafe.config.ConfigFactory
import io.ktor.config.HoconApplicationConfig
import io.ktor.http.content.default
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.http.content.staticRootFolder
import io.ktor.routing.Route
import io.ktor.routing.route
import io.ktor.util.KtorExperimentalAPI
import se.jensim.testinggraounds.ktor.server.healthcheck.healthcheck
import java.io.File

fun Route.root() {
    route("api") {
        // healthcheck()
    }
    static("/") {
        val conf = HoconApplicationConfig(ConfigFactory.load())
        val env = conf.config("ktor.deployment").property("environment").getString()
        staticRootFolder = if (env == "production") {
            File(javaClass.getResource("/frontend").toURI())
        } else {
            // Assume dev mode
            File("src/main/resources/frontend")
        }
        files(".")
        default("index.html")
    }
    healthcheck()
}
