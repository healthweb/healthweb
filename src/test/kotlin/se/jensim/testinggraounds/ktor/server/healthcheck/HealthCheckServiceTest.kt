package se.jensim.testinggraounds.ktor.server.healthcheck

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import se.jensim.shared.models.HealthCheck
import se.jensim.shared.models.HealthCheckEndpoint
import se.jensim.shared.models.HealthChecks
import se.jensim.testinggraounds.ktor.server.config.TestMongoConfig.tempCollection

class HealthCheckServiceTest {

    private val endpoint = HealthCheckEndpoint.fromUrl("http://localhost:8080/health")
    private val answer = HealthChecks(mapOf("Database" to HealthCheck("Feeling splendid", true)))

    @Test
    fun crawlAll() {
        //given
        val mock: HealthcheckCrawler = mock {
            onBlocking { crawl(endpoint.url) } doReturn answer
        }
        tempCollection(HealthCheckEndpoint::class.java) {
            val service = HealthCheckService(it, mock)
            assertThat(it.countDocuments(), equalTo(0L))
            it.insertOne(endpoint)
            assertThat(it.countDocuments(), equalTo(1L))

            // when
            service.crawlAll()

            // then
            assertThat(it.countDocuments(), equalTo(1L))
            val doc = it.findOne()!!
            assertThat(doc.lastResponse, equalTo(answer))
        }
    }
}
