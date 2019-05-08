package se.jensim.testinggraounds.ktor.server.healthcheck

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.get
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readText
import org.slf4j.LoggerFactory
import se.jensim.shared.models.DropwizardHealthCheck
import se.jensim.shared.models.HealthCheckEndpoint
import se.jensim.shared.models.HealthChecks
import se.jensim.testinggraounds.ktor.server.config.ObjectMapperConfig

class HealthcheckCrawler(private val client: HttpClient, private val ob: ObjectMapper) {

    private val log = LoggerFactory.getLogger(javaClass)
    private val typeRef = object : TypeReference<DropwizardHealthCheck>() {}

    suspend fun crawl(endpoint: HealthCheckEndpoint): HealthChecks = try {
        log.debug("Crawling ${endpoint.url}")
        val resp: HttpResponse = client.get(endpoint.url)
        val respText = resp.readText()
        HealthChecks(ob.readValue(respText, typeRef))
    } catch (e: Exception) {
        log.debug("Failed crawling ${endpoint.url}")
        HealthChecks(null)
    }
}

object HealthCheckCrawlerSingleton {

    private val client: HttpClient by lazy {
        HttpClient(Apache) {
            expectSuccess = false
            engine {
                socketTimeout = 1_000
                connectTimeout = 1_000
                connectionRequestTimeout = 2_000

            }
        }
    }
    val singleton by lazy { HealthcheckCrawler(client, ObjectMapperConfig.config()) }
}
