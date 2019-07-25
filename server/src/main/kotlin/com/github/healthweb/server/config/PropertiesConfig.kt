package com.github.healthweb.server.config

import com.typesafe.config.ConfigFactory
import io.ktor.config.HoconApplicationConfig

object PropertiesConfig {

    private val conf = HoconApplicationConfig(ConfigFactory.load())

    fun isProd(): Boolean = PropertiesConfig::class.java
            .protectionDomain?.codeSource?.location?.file?.endsWith(".jar") == true

    fun dbUrl(): String = conf.config("db").property("url").getString()
    fun dbDriver(): String = conf.config("db").property("driver").getString()
    fun dbUser(): String = conf.config("db").property("user").getString()
    fun dbPassword(): String = conf.config("db").property("password").getString()
    fun dbCreateTables(): Boolean = conf.config("db").property("create_tables").getString().toLowerCase() == "true"

    fun probeTimeIntervalMilli(): Long = conf.config("probe").property("interval_sec").getString().toLong().times(1_000)
    fun probeTimeout(): Long = conf.config("probe").property("timeout").getString().toLong()
}
