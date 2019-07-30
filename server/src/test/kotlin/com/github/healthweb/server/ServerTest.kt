package com.github.healthweb.server

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
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
import kotlin.test.fail

@ExperimentalStdlibApi
class ServerTest {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Rule
    @JvmField
    val wireMockRule = WireMockRule()

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
                handleRequest(HttpMethod.Post, "/health") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody("{\"url\": \"$hcUrl\"}")
                }.apply {
                    assertEquals(200, response.status()?.value)
                    assertThat(response.content, containsString(hcUrl))
                    assertThat(response.content, containsString("\"status\" : \"UNVERIFIED\""))
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
