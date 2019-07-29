package com.github.healthweb.server.healthcheck

import com.github.healthweb.server.config.DatabaseConfig
import com.github.healthweb.server.config.PropertiesConfig
import com.github.healthweb.server.websockets.WebSocketService
import com.github.healthweb.shared.HealthCheck
import com.github.healthweb.shared.HealthCheckEndpoint
import com.github.healthweb.shared.HealthChecks
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matchers
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert.assertThat
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
    fun crawlAllTest() {
        //given
        DatabaseConfig.singleton.init()
        val mockCrawler: HealthCheckCrawler = mock {
            onBlocking { crawlAsync(endpoint.url) } doReturn GlobalScope.async { answer }
        }
        val mockWebSocket: WebSocketService = mock()
        val service = HealthCheckService(mockCrawler, mockWebSocket, PropertiesConfig.singleton)
        transaction {
            assertThat(HealthCheckEndpointDao.count(), equalTo(0))
            HealthCheckEndpointDao.new {
                url = "http://localhost:8080/health"
            }
            assertThat(HealthCheckEndpointDao.count(), equalTo(1))
        }

        // when
        runBlocking {
            service.crawlAll()
        }

        // then
        transaction {
            val all = HealthCheckEndpointDao.all().toList()
            assertThat(all.size, equalTo(1))
            assertThat(all.first().last_response, Matchers.containsString("\"healthy\" : true"))
        }
    }
}
