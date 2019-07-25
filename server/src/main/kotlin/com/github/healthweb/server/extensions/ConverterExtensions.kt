package com.github.healthweb.server.extensions

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.healthweb.server.config.ObjectMapperConfig
import com.github.healthweb.server.healthcheck.HealthCheckEndpointDao
import com.github.healthweb.shared.HealthCheckEndpoint
import com.github.healthweb.shared.HealthChecks

fun HealthChecks.toJson(om: ObjectMapper? = null): String = ObjectMapperConfig.config(om).writeValueAsString(this)

fun String.toHealthChecks(om: ObjectMapper? = null): HealthChecks = ObjectMapperConfig.config(om).readValue(this, HealthChecks::class.java)

fun HealthCheckEndpointDao.toDto() = HealthCheckEndpoint(
        _id = this.id.value,
        url = this.url,
        probeIntervalOverride = this.probeIntervalOverride,
        lastProbeTime = this.lastProbeTime,
        lastProblemTime = this.lastProblemTime,
        status = this.status,
        tags = this.tags,
        lastResponse = this.last_response?.toHealthChecks()
)

