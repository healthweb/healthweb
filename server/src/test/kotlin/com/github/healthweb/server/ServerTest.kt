package com.github.healthweb.server

import com.github.healthweb.server.config.ObjectMapperConfig
import com.github.healthweb.shared.Dashboard
import com.github.healthweb.shared.HealthCheckEndpoint
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.github.tomakehurst.wiremock.junit.WireMockRule
import io.ktor.application.Application
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import org.hamcrest.Matchers
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.containsString
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.slf4j.LoggerFactory
import java.util.ArrayList
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.fail

@ExperimentalStdlibApi
class ServerTest {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val objectMapper = ObjectMapperConfig.config()

    @Rule
    @JvmField
    val wireMockRule = WireMockRule(wireMockConfig().port(8089))

    @Before
    fun setUp() {
        wireMockRule.start()
        wireMockRule.resetAll()
        wireMockRule.stubFor(get(urlEqualTo("/healthcheck"))
                .withHeader("Accept", equalTo("*/*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            {
                              "Database": {
                                            "healthy": true,
                                            "message": "all ok"
                                          }
                            }
                        """.trimIndent())))
    }

    @After
    fun tearDown() {
        wireMockRule.stop()
    }

    @Test(timeout = 10_000)
    fun testRoutes() {
        logger.info("WireMock server running on port ${wireMockRule.port()}")
        val hcUrl = "http://localhost:${wireMockRule.port()}/healthcheck"
        withTestApplication(Application::mainModule) {
            val wsResponse = ArrayList<String>()
            handleWebSocketConversation("/health", {}, { i, _ ->

                val dashboard = handleRequest(HttpMethod.Post, "/dashboard") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody("""
                        {"name": "Hejkon",
                        "description": "Bejkon"}
                    """.trimIndent())
                }.let {
                    assertEquals(200, it.response.status()?.value)
                    assertNotNull(it.response.content)
                    objectMapper.readValue(it.response.content, Dashboard::class.java)
                }
                val hc = handleRequest(HttpMethod.Post, "/health") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody("{\"url\": \"$hcUrl\"}")
                }.let {
                    assertEquals(200, it.response.status()?.value)
                    assertThat(it.response.content, containsString(hcUrl))
                    assertThat(it.response.content, containsString("\"status\" : \"UNVERIFIED\""))
                    objectMapper.readValue(it.response.content, HealthCheckEndpoint::class.java)
                }
                handleRequest(HttpMethod.Post, "/dashboard/link") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody("""
                        {"dashboardId": ${dashboard.id},
                        "healthCheckId": ${hc._id}}
                    """.trimIndent())
                }.apply {
                    assertEquals(200, response.status()?.value)
                }

                try {
                    withTimeout(5000) {
                        for (r in i) {
                            val string = String(r.data)
                            wsResponse.add(string)
                            if (wsResponse.size == 2) {
                                break
                            }
                        }
                    }
                } catch (e: TimeoutCancellationException) {
                    fail("Timed out")
                }

            })
            assertThat(wsResponse, contains(
                    Matchers.allOf(
                            containsString("\"status\" : \"UNVERIFIED\""),
                            containsString(hcUrl)
                    ),
                    Matchers.allOf(
                            containsString("\"status\" : \"HEALTHY\""),
                            containsString(hcUrl)
                    )
            ))
        }
    }
}
