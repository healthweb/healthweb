package se.jensim.testinggraounds.ktor.shared

@ToTypeScript
data class HealthCheckEndpoint(
    val _id: Long? = null,
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
