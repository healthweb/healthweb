package com.github.healthweb.shared

@ToTypeScript
data class Dashboard (
        val id: Long?,
        val name: String,
        val description: String,
        val healthchecks: List<Long> = emptyList(), // IDs
        val archived: Boolean = false
)
