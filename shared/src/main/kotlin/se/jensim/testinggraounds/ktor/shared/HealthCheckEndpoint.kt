package se.jensim.testinggraounds.ktor.shared

@ToTypeScript
data class HealthCheckEndpoint(
    val _id: String? = null,
    val url: String,
    val lastResponse: HealthChecks? = null,
    val status: ServiceStatus = ServiceStatus.UNVERIFIED,
    val lastProbeTime: Long? = null,
    val lastProblemTime: Long? = null,
    val probeIntervalOverride: Long? = null,
    val tags: List<String> = emptyList()

) {
    companion object {
        fun fromUrl(url: String) = HealthCheckEndpoint(url = url)
    }
}
typealias  DropwizardHealthCheck = Map<String, HealthCheck>

data class HealthChecks(val checks: DropwizardHealthCheck?) {
    fun isHealthy(): Boolean = checks?.all { (_, c) -> c.healthy } ?: false
}

data class HealthCheck(
    val message: String,
    val healthy: Boolean,
    val error: HealthCheckError? = null
)

data class HealthCheckError(
    val message: String,
    val stack: List<String>
)

enum class ServiceStatus {
    UNVERIFIED,
    HEALTHY,
    UNHEALTHY,
    UNRESPONSIVE,
    UNSTABLE,
    UNKNOWN,
}
