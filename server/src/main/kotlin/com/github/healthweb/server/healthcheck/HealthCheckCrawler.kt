package com.github.healthweb.server.healthcheck

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.healthweb.server.config.ObjectMapperConfig
import com.github.healthweb.server.config.PropertiesConfig
import com.github.healthweb.shared.DropwizardHealthCheck
import com.github.healthweb.shared.HealthChecks
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.get
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readText
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withTimeout
import org.slf4j.LoggerFactory

open class HealthCheckCrawler(private val client: HttpClient, private val ob: ObjectMapper) {

    companion object {

        val singleton by lazy {
            val client = HttpClient(Apache) {
                expectSuccess = false
                engine {
                    socketTimeout = 1_000
                    connectTimeout = 1_000
                    connectionRequestTimeout = 2_000

                }
            }
            HealthCheckCrawler(client, ObjectMapperConfig.config())
        }
    }

    private val log = LoggerFactory.getLogger(javaClass)
    private val typeRef = object : TypeReference<DropwizardHealthCheck>() {}

    open suspend fun crawlAsync(endpoint: String): Deferred<HealthChecks> = GlobalScope.async(Dispatchers.IO) {
        try {
            withTimeout(PropertiesConfig.probeTimeout()) {
                log.debug("Crawling $endpoint")
                val resp: HttpResponse = client.get(endpoint)
                val respText = resp.readText()
                @Suppress("BlockingMethodInNonBlockingContext")
                val a: DropwizardHealthCheck = ob.readValue(respText, typeRef)
                HealthChecks(a)
            }
        } catch (e: Exception) {
            log.debug("Failed crawling $endpoint")
            HealthChecks(null)
        }
    }
}
