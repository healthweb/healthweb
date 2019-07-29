package com.github.healthweb.server.config

import com.typesafe.config.ConfigFactory
import io.ktor.config.HoconApplicationConfig

class PropertiesConfig {

    companion object {
        val singleton = PropertiesConfig()
    }

    private val conf by lazy { HoconApplicationConfig(ConfigFactory.load()) }

    val port by lazy { conf.config("ktor.deployment").property("port").getString().toInt() }
    val isProd: Boolean by lazy { PropertiesConfig::class.java.protectionDomain?.codeSource?.location?.file?.endsWith(".jar") == true }

    val dbUrl: String by lazy { conf.config("db").property("url").getString() }
    val dbDriver: String by lazy { conf.config("db").property("driver").getString() }
    val dbUser: String by lazy { conf.config("db").property("user").getString() }
    val dbPassword: String by lazy { conf.config("db").property("password").getString() }
    val dbCreateTables: Boolean by lazy { conf.config("db").property("create_tables").getString().toLowerCase() == "true" }

    val probeTimeIntervalMilli: Long by lazy { conf.config("probe").property("interval_sec").getString().toLong().times(1_000) }
    val probeTimeout: Long by lazy { conf.config("probe").property("timeout").getString().toLong() }
}
