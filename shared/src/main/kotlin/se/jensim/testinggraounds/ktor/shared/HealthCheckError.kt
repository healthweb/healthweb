package se.jensim.testinggraounds.ktor.shared

data class HealthCheckError(
        val message: String,
        val stack: List<String>
)
