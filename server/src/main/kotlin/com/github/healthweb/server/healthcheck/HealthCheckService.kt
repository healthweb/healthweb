package com.github.healthweb.server.healthcheck

import com.mongodb.client.model.Filters
import kotlinx.coroutines.*
import org.bson.Document
import org.bson.conversions.Bson
import org.litote.kmongo.coroutine.CoroutineCollection
import org.slf4j.LoggerFactory
import com.github.healthweb.server.config.MongoConfig
import com.github.healthweb.server.websockets.WebSocketService
import com.github.healthweb.shared.HealthCheckEndpoint
import com.github.healthweb.shared.HealthChecks
import com.github.healthweb.shared.ServiceStatus
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.max

class HealthCheckService(
    private val mongoCollection: CoroutineCollection<HealthCheckEndpoint>,
    private val crawler: HealthcheckCrawler,
    private val webSocketService: WebSocketService
) {

    companion object {
        private val isRunning = AtomicBoolean(false)
        private const val stabilityBreakpoint = 300_000L

        val singleton by lazy {
            HealthCheckService(
                MongoConfig.healthCheckEndpointCollection,
                HealthcheckCrawler.singleton,
                WebSocketService.singleton
            )
        }
    }

    private val log = LoggerFactory.getLogger(javaClass)
    private val coroutineContext = newSingleThreadContext("HealthCheckService")

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
                    if (it.lastProbeTime != null && it.probeIntervalOverride != null && (it.lastProbeTime as Long + it.probeIntervalOverride as Long) < System.currentTimeMillis()) {
                        log.trace("Not yet time to check this service ${it.url}, probeIntervalOverride defined but not yet reached.")
                        return@consumeEach
                    }
                    val newResult = crawler.crawl(it.url)
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
        val currentTimeMillis = System.currentTimeMillis()
        val updated = hc.copy(
            status = status(hc, newResult),
            lastProbeTime = currentTimeMillis,
            lastResponse = newResult,
            lastProblemTime = if (newResult.isHealthy()) hc.lastProblemTime else currentTimeMillis
        )

        mongoCollection.save(updated)
        webSocketService.broadcast(updated)
    }

    fun status(hc: HealthCheckEndpoint, newResult: HealthChecks): ServiceStatus =
        if (newResult.checks == null) {
            if (hc.status == ServiceStatus.UNVERIFIED) {
                ServiceStatus.UNVERIFIED
            } else {
                ServiceStatus.UNRESPONSIVE
            }
        } else if (newResult.isHealthy()) {
            if (hc.lastProblemTime ?: 0 < System.currentTimeMillis() - stabilityBreakpoint) {
                ServiceStatus.HEALTHY
            } else {
                ServiceStatus.UNSTABLE
            }
        } else {
            ServiceStatus.UNHEALTHY
        }


    fun launchCrawler() {
        GlobalScope.launch(coroutineContext) {
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

    suspend fun saveNew(hc: HealthCheckEndpoint): HealthCheckEndpoint {
        mongoCollection.insertOne(hc)
        return mongoCollection.findOne(Document("url", hc.url))!!
    }
}
