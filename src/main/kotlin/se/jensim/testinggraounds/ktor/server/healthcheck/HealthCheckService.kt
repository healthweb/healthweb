package se.jensim.testinggraounds.ktor.server.healthcheck

import com.mongodb.DuplicateKeyException
import com.mongodb.client.model.Filters
import kotlinx.coroutines.*
import org.bson.conversions.Bson
import org.litote.kmongo.coroutine.CoroutineCollection
import org.slf4j.LoggerFactory
import se.jensim.shared.models.HealthCheckEndpoint
import se.jensim.shared.models.HealthChecks
import se.jensim.shared.models.ServiceStatus
import se.jensim.testinggraounds.ktor.server.config.MongoConfig
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.max

class HealthCheckService(
    private val mongoCollection: CoroutineCollection<HealthCheckEndpoint>,
    private val crawler: HealthcheckCrawler
) {

    companion object {
        private val isRunning = AtomicBoolean(false)
    }

    private val log = LoggerFactory.getLogger(javaClass)

    private fun filter(): Bson {
        return Filters.or(
            Filters.eq("lastProbeTime", null),
            Filters.and(
                Filters.eq("probeIntervalOverride", null),
                Filters.lt("lastProbeTime", System.currentTimeMillis() - 60_000)
            ),
            Filters.and(
                Filters.not(
                    Filters.eq("probeIntervalOverride", null)
                ),
                Filters.lt("lastProbeTime", System.currentTimeMillis() - 5_000)
            )
        )
    }

    suspend fun crawlAll() {
        val doRun = isRunning.compareAndSet(false, true)
        try {
            if (doRun) {
                mongoCollection.find(filter()).consumeEach {
                    if (it.lastProbeTime != null && it.probeIntervalOverride != null && TODO()) {
                        return@consumeEach
                    }
                    val newResult = crawler.crawl(it)
                    update(it, newResult)
                }
            }
        } catch (e: Exception) {
            log.error("A terribad accident occured!", e)
        } finally {
            if (doRun) {
                isRunning.set(false)
            }
        }
    }

    suspend fun update(hc: HealthCheckEndpoint, newResult: HealthChecks) {
        val updated = hc.copy(
            status = status(hc, newResult),
            lastProbeTime = System.currentTimeMillis()
        )

        mongoCollection.save(updated)
    }

    fun status(hc: HealthCheckEndpoint, newResult: HealthChecks): ServiceStatus {
        TODO()
    }

    fun launchCrawler() {
        runBlocking {
            launch {
                val timeMillis = 10_000L
                while (true) {
                    val start = System.currentTimeMillis()
                    try {
                        withTimeout(timeMillis) {
                            crawlAll()
                        }
                    } catch (e: TimeoutCancellationException) {
                        log.warn("Crawl took more than $timeMillis ms")
                    } catch (e: Exception) {
                        log.error("This should never occur. Im truly sad.", e)
                    }
                    val spent = System.currentTimeMillis() - start
                    val left = timeMillis - spent
                    delay(max(0, left))
                }
            }
        }
    }

    suspend fun saveNew(hc: HealthCheckEndpoint) {
        try {
            mongoCollection.insertOne(hc)
        } catch (e: DuplicateKeyException) {
            log.debug("Url already registered")
        }
    }
}


object HealthCheckServiceSingleton {
    val singleton by lazy {
        HealthCheckService(
            MongoConfig.healthCheckEndpointCollection,
            HealthCheckCrawlerSingleton.singleton
        )
    }
}
