package com.github.healthweb.shared

data class Dashboard (
        val id: Long,
        val name: String,
        val description: String,
        val healthchecks: List<Long> // IDs
)
