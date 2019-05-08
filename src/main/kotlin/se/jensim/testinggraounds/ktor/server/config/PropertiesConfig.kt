package se.jensim.testinggraounds.ktor.server.config

import com.typesafe.config.ConfigFactory
import io.ktor.config.HoconApplicationConfig

object PropertiesConfig {

    private val conf = HoconApplicationConfig(ConfigFactory.load())

    fun isProd(): Boolean {
        val env = conf.config("ktor.deployment").property("environment").getString()
        return env == "production"
    }

    fun mongoUrl(): String = conf.config("mongo").property("url").getString()
}
