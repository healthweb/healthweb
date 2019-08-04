package com.github.healthweb.server.dashboard

import com.github.healthweb.server.healthcheck.HealthCheckEndpointDao
import com.github.healthweb.server.healthcheck.HealthCheckEndpointTable
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.Table

object DashboardTable : LongIdTable("DASHBOARD"){
    val name = varchar("NAME", 250)
    val description = varchar("DESCRIPTION", 10000)
    val archived = bool("ARCHIVED").default(false)
}

class DashboardDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<DashboardDao>(DashboardTable)

    var name by DashboardTable.name
    var description by DashboardTable.description
    var archived by DashboardTable.archived
    var healthchecks by HealthCheckEndpointDao via HealthcheckDashboardTable
}

object HealthcheckDashboardTable: Table("HEALTHCHECK_DASHBOARD") {
    val healthcheck = reference("healthcheck", HealthCheckEndpointTable).primaryKey(0)
    val dashboard = reference("dashboard", DashboardTable).primaryKey(1)
}
