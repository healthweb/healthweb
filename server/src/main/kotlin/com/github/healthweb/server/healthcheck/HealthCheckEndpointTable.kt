package com.github.healthweb.server.healthcheck

import com.github.healthweb.shared.ServiceStatus
import com.github.healthweb.shared.ServiceStatus.UNVERIFIED
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.Table

object HealthCheckEndpointTable : LongIdTable("HEALTH_CHECK_ENDPOINTS") {
    val url = varchar("url", 512).uniqueIndex()
    val last_response = varchar("last_response", 10_000).nullable()
    val status = enumeration("status", ServiceStatus::class).default(UNVERIFIED)
    val lastProbeTime = long("last_probe_time").nullable()
    val lastProblemTime = long("last_problem_time").nullable()
    val probeIntervalOverride = long("probe_interval_override").nullable()
}

class HealthCheckEndpointDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<HealthCheckEndpointDao>(HealthCheckEndpointTable)

    var url by HealthCheckEndpointTable.url
    var last_response by HealthCheckEndpointTable.last_response
    var status by HealthCheckEndpointTable.status
    var lastProbeTime by HealthCheckEndpointTable.lastProbeTime
    var lastProblemTime by HealthCheckEndpointTable.lastProblemTime
    var probeIntervalOverride by HealthCheckEndpointTable.probeIntervalOverride
}

object EndpointTagsTable : Table("ENDPOINT_TAGS") {
    val endpoint = reference("endpoint", HealthCheckEndpointTable).primaryKey(0)
    val tag = varchar("tag", 256).primaryKey(1)
}
