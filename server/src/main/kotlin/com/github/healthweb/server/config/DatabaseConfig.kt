package com.github.healthweb.server.config

import com.github.healthweb.server.healthcheck.HealthCheckEndpointTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

object DatabaseConfig {

    private val log = LoggerFactory.getLogger(javaClass)

    val db: Database by lazy {
        Database.connect(PropertiesConfig.dbUrl(),
                driver = PropertiesConfig.dbDriver(),
                user = PropertiesConfig.dbUser(),
                password = PropertiesConfig.dbPassword())
    }

    fun init() {
        db.run {
            log.debug("Connected to database.")
        }
        if (PropertiesConfig.dbCreateTables()) {
            log.debug("Creating tables")
            transaction {
                SchemaUtils.create(HealthCheckEndpointTable)
            }
        }
    }
}
