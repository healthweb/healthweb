package com.github.healthweb.server.healthcheck

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import com.github.healthweb.server.config.TestMongoConfig.tempCollection
import com.github.healthweb.server.websockets.WebSocketService
import kotlin.test.Test

class HealthCheckServiceTest {

    private val endpoint = HealthCheckEndpoint.fromUrl("http://localhost:8080/health")
    private val answer = HealthChecks(
        mapOf(
            "Database" to HealthCheck(
                "Feeling splendid",
                true
            )
        )
    )

    @Test
    fun crawlAll() {
        //given
        val mockCrawler: HealthcheckCrawler = mock {
            onBlocking { crawl(endpoint.url) } doReturn answer
        }
        val mockWebSocket: WebSocketService = mock()
        tempCollection(HealthCheckEndpoint::class.java) {
            val service = HealthCheckService(it, mockCrawler, mockWebSocket)
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
