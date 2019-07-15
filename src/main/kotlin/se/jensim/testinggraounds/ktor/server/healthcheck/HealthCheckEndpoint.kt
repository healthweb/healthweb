package se.jensim.testinggraounds.ktor.server.healthcheck

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import se.jensim.testinggraounds.ktor.server.config.ToTypeScript

@ToTypeScript
data class HealthCheckEndpoint(
    @BsonId
    @BsonProperty
    val _id: String? = null,
    @BsonProperty
    val url: String,
    @BsonProperty
    val lastResponse: HealthChecks? = null,
    @BsonProperty
    val status: ServiceStatus = ServiceStatus.UNVERIFIED,
    @BsonProperty
    val lastProbeTime: Long? = null,
    @BsonProperty
    val lastProblemTime: Long? = null,
    @BsonProperty
    val probeIntervalOverride: Long? = null,
    @BsonProperty
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
    @BsonProperty
    val message: String,
    @BsonProperty
    val healthy: Boolean,
    @BsonProperty
    val error: HealthCheckError? = null
)

data class HealthCheckError(
    @BsonProperty
    val message: String,
    @BsonProperty
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
