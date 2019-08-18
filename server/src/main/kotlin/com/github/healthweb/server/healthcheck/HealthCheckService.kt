package com.github.healthweb.server.healthcheck

import com.github.healthweb.server.config.PropertiesConfig
import com.github.healthweb.server.extensions.toDto
import com.github.healthweb.server.extensions.toJson
import com.github.healthweb.server.websockets.WebSocketService
import com.github.healthweb.shared.HealthCheckEndpoint
import com.github.healthweb.shared.HealthChecks
import com.github.healthweb.shared.ServiceStatus
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withTimeout
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.max

@ExperimentalStdlibApi
class HealthCheckService(
        private val crawler: HealthCheckCrawler,
        private val webSocketService: WebSocketService,
        private val properties: PropertiesConfig
) {

    companion object {
        private val isRunning = AtomicBoolean(false)
        private const val stabilityBreakpoint = 300_000L

        val singleton by lazy {
            HealthCheckService(
                    HealthCheckCrawler.singleton,
                    WebSocketService.singleton,
                    PropertiesConfig.singleton
            )
        }
    }

    private val log = LoggerFactory.getLogger(javaClass)
    private val coroutineContext = newSingleThreadContext("HealthCheckService")

    private fun filtered() = transaction {
        val time = System.currentTimeMillis()
        val regularProbeCutOf = time - properties.probeTimeIntervalMilli
        HealthCheckEndpointDao.find {
            HealthCheckEndpointTable.lastProbeTime.isNull() or
                    (HealthCheckEndpointTable.probeIntervalOverride.isNull() and (HealthCheckEndpointTable.lastProbeTime less regularProbeCutOf)) or
                    (HealthCheckEndpointTable.probeIntervalOverride.isNotNull() and (HealthCheckEndpointTable.lastProbeTime.plus(HealthCheckEndpointTable.probeIntervalOverride) less time))
        }.toList()
    }

    suspend fun crawlAll() {
        val doRun = isRunning.compareAndSet(false, true)
        try {
            if (doRun) {
                val waiting = filtered()
                        .map { mapNewResult(it) }
                waiting.forEach { update(it.first, it.second.await()) }
            }else{
                log.warn("Prevented concurrent crawl")
            }
        } catch (e: Exception) {
            log.error("A terribad accident occured!", e)
        } finally {
            if (doRun) {
                isRunning.set(false)
            }
        }
    }

    private suspend fun mapNewResult(hc: HealthCheckEndpointDao): Pair<HealthCheckEndpointDao, Deferred<HealthChecks>> {
        transaction {
            hc.lastProbeTime = System.currentTimeMillis()
        }
        return hc to crawler.crawlAsync(hc.url)
    }

    suspend fun update(hc: HealthCheckEndpointDao, newResult: HealthChecks) {
        val currentTimeMillis = System.currentTimeMillis()
        val dto = transaction {
            hc.status = status(hc, newResult)
            if (!newResult.isHealthy()) {
                hc.lastProblemTime = currentTimeMillis
            }
            hc.last_response = newResult.toJson()
            hc.toDto()
        }
        webSocketService.broadcast(dto)
    }

    private fun status(hc: HealthCheckEndpointDao, newResult: HealthChecks): ServiceStatus =
            if (newResult == null) {
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
            delay(500)
            while (true) {
                val start = System.currentTimeMillis()
                try {
                    withTimeout(timeMillis) {
                        log.trace("Starting probe")
                        crawlAll()
                    }
                } catch (e: TimeoutCancellationException) {
                    log.warn("Crawl took more than $timeMillis ms")
                } catch (e: Exception) {
                    log.error("This should never occur. Im truly sad.", e)
                }
                val spent = System.currentTimeMillis() - start
                val left = timeMillis - spent
                log.trace("Probe done, will sleep for $left")
                delay(max(0, left))
            }
        }
    }

    fun getAllAsync(): Deferred<List<HealthCheckEndpoint>> = GlobalScope.async(IO){
        transaction { HealthCheckEndpointDao.all().map { it.toDto() } }
    }

    fun saveNew(hc: HealthCheckEndpoint): HealthCheckEndpoint = transaction {
        URL(hc.url) // Guard against malformed urls
        val existing = HealthCheckEndpointDao.find { HealthCheckEndpointTable.url.eq(hc.url) }
        if(existing.count() == 0){
            HealthCheckEndpointDao.new {
                url = hc.url
            }
        }else{
            existing.first()
        }.toDto()
    }

    fun addTag(id:Long, tagg:String)= transaction {
        EndpointTagsTable.insert {
            it[endpoint] = EntityID(id, HealthCheckEndpointTable)
            it[tag] = tagg.toLowerCase()
        }
        HealthCheckEndpointDao.findById(id)!!.toDto()
    }

    fun removeTag(id:Long, tagg:String) = transaction {
        EndpointTagsTable.deleteWhere(1) {
            EndpointTagsTable.endpoint.eq(id) and EndpointTagsTable.tag.eq(tagg)
        }
        HealthCheckEndpointDao.findById(id)!!.toDto()
    }
}
