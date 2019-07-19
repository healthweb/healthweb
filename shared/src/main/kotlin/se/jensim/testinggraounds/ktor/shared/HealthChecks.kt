package se.jensim.testinggraounds.ktor.shared

data class HealthChecks(val checks: DropwizardHealthCheck?) {
    fun isHealthy(): Boolean = checks?.all { (_, c) -> c.healthy } ?: false
}
