package com.github.healthweb.server.config

import com.typesafe.config.ConfigFactory
import io.ktor.config.HoconApplicationConfig

object PropertiesConfig {

    private val conf = HoconApplicationConfig(ConfigFactory.load())

    fun isProd(): Boolean = PropertiesConfig::class.java
            .protectionDomain?.codeSource?.location?.file?.endsWith(".jar") == true

    fun mongoUrl(): String = conf.config("mongo").property("url").getString()
}
