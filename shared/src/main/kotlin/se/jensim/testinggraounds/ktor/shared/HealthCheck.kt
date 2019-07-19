package se.jensim.testinggraounds.ktor.shared

data class HealthCheck(
        val message: String,
        val healthy: Boolean,
        val error: HealthCheckError? = null
)
