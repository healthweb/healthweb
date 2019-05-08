package se.jensim.testinggraounds.ktor.server.healthcheck

import com.github.fakemongo.junit.FongoAsyncRule
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.litote.kmongo.coroutine.CoroutineClient
import se.jensim.shared.models.DropwizardHealthCheck
import se.jensim.shared.models.HealthCheck
import se.jensim.shared.models.HealthCheckEndpoint

class HealthCheckServiceTest {

    @Rule
    @JvmField
    val mongoRule = FongoAsyncRule("test")
    private val patchedClient: MongoClient = MongoClients.create(mongoRule.mongoClient)
    private val cClient: CoroutineClient = CoroutineClient(patchedClient)
    private val cDb = cClient.getDatabase("test")
    private val cCol = cDb.getCollection<HealthCheckEndpoint>()
    private val answer: DropwizardHealthCheck = mapOf("Database" to HealthCheck("Feeling splendid", true))

    @Test
    fun crawlAll() {
        runBlocking {
            //given
            assertThat(cCol.countDocuments(), equalTo(0L))
            val mock: HealthcheckCrawler = mock {
                on { runBlocking { crawl(any()) } } doReturn answer
            }
            val service = HealthCheckService(cCol, mock)

            // when
            service.crawlAll()

            // then
            assertThat(cCol.countDocuments(), equalTo(1L))

        }
    }
}
