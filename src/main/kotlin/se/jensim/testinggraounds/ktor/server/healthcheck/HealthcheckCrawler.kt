package se.jensim.testinggraounds.ktor.server.healthcheck

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.get
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readText
import se.jensim.shared.models.DropwizardHealthCheck
import se.jensim.testinggraounds.ktor.server.config.ObjectMapperConfig

class HealthcheckCrawler(private val client: HttpClient, val ob: ObjectMapper) {

    private val typeRef = object :TypeReference<DropwizardHealthCheck>(){}

    suspend fun crawl(endpoint: HealthcheckEndpoint): DropwizardHealthCheck {
        val resp: HttpResponse = client.get(endpoint.url)
        return ob.readValue(resp.readText(), typeRef)
    }
}

object HealthCheckCrawlerService {

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
    val service by lazy { HealthcheckCrawler(client, ObjectMapperConfig.config()) }
}
