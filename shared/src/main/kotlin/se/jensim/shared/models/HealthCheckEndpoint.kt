package se.jensim.shared.models

data class HealthCheckEndpoint(
    val url: String,
    val lastResponse: DropwizardHealthCheck? = null
)
typealias  DropwizardHealthCheck = Map<String, HealthCheck>

data class HealthCheck(val message: String, val healthy: Boolean, val error: HealthCheckError? = null)
data class HealthCheckError(val message: String, val stack: List<String>)
