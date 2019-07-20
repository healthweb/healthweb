package com.github.healthweb.server

import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.hamcrest.CoreMatchers
import org.junit.Assert
import kotlin.test.Test
import kotlin.test.assertEquals

class ServerTest {

    @Test
    fun testRoutes() {
        withTestApplication(Application::mainModule) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(200, response.status()?.value)
                Assert.assertThat(response.content, CoreMatchers.containsString("<title>Frontend</title>"))
            }
        }
    }
}
