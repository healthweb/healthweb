package com.github.healthweb.shared

data class HealthCheckError(
        val message: String,
        val stack: List<String>
)
