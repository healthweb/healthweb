package com.github.healthweb.server.extensions

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.healthweb.server.config.ObjectMapperConfig
import com.github.healthweb.server.dashboard.DashboardDao
import com.github.healthweb.server.healthcheck.HealthCheckEndpointDao
import com.github.healthweb.shared.Dashboard
import com.github.healthweb.shared.HealthCheckEndpoint
import com.github.healthweb.shared.HealthChecks

fun HealthChecks.toJson(om: ObjectMapper? = null): String = ObjectMapperConfig.config(om).writeValueAsString(this)

fun String.toHealthChecks(om: ObjectMapper? = null): HealthChecks = ObjectMapperConfig.config(om).readValue(this, HealthChecks::class.java)

fun HealthCheckEndpointDao.toDto() = HealthCheckEndpoint(
        id = this.id.value,
        url = this.url,
        probeIntervalOverride = this.probeIntervalOverride,
        lastProbeTime = this.lastProbeTime,
        lastProblemTime = this.lastProblemTime,
        status = this.status,
        lastResponse = this.last_response?.toHealthChecks()
)

fun HealthCheckEndpointDao.toJson(om: ObjectMapper? = null) = toDto().toJson(om)

/**
 * Needs a transaction to run
 */
fun DashboardDao.toDto() = Dashboard(
        id = id.value,
        name = name,
        description = description,
        healthchecks = healthchecks.map { it.id.value },
        archived = archived
)

fun Dashboard.toJson(om: ObjectMapper? = null): String = ObjectMapperConfig.config(om).writeValueAsString(this)

fun DashboardDao.toJson(om: ObjectMapper? = null): String = toDto().toJson(om)

fun HealthCheckEndpoint.toJson(om: ObjectMapper? = null): String = ObjectMapperConfig.config(om).writeValueAsString(this)
