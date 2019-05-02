package se.jensim.testinggraounds.ktor.server.healthcheck

import com.fasterxml.jackson.core.type.TypeReference

typealias DropwizardHealthCheck = Map<String, HealthCheck>

val typeRef = object : TypeReference<DropwizardHealthCheck>() {}

data class HealthCheck(val message: String, val healthy: Boolean, val error: HealthCheckError? = null)
data class HealthCheckError(val message:String, val stack: List<String>)
