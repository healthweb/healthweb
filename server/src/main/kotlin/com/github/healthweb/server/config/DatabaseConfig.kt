package com.github.healthweb.server.config

import com.github.healthweb.server.dashboard.DashboardTable
import com.github.healthweb.server.dashboard.HealthcheckDashboardTable
import com.github.healthweb.server.healthcheck.EndpointTagsTable
import com.github.healthweb.server.healthcheck.HealthCheckEndpointTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class DatabaseConfig(private val properties: PropertiesConfig) {

    companion object {
        val singleton = DatabaseConfig(PropertiesConfig.singleton)
    }

    private val log = LoggerFactory.getLogger(javaClass)

    val db: Database by lazy {
        Database.connect(properties.dbUrl,
                driver = properties.dbDriver,
                user = properties.dbUser,
                password = properties.dbPassword)
    }

    fun init() {
        db.run {
            log.debug("Connected to database.")
        }
        if (properties.dbCreateTables) {
            log.debug("Creating tables")
            transaction {
                SchemaUtils.create(HealthCheckEndpointTable, DashboardTable, HealthcheckDashboardTable, EndpointTagsTable)
            }
        }
    }
}
