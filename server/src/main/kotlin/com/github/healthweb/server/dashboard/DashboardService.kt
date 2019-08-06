package com.github.healthweb.server.dashboard

import com.github.healthweb.server.extensions.toDto
import com.github.healthweb.server.healthcheck.HealthCheckEndpointDao
import com.github.healthweb.shared.Dashboard
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class DashboardService {

    companion object {
        val singleton = DashboardService()
        private val log = LoggerFactory.getLogger(DashboardService::class.java)
    }

    fun getAllAsync(): Deferred<List<Dashboard>> = GlobalScope.async(IO) {
        log.debug("Get all dashboards")
        transaction {
            DashboardDao.all().map { it.toDto() }
        }
    }

    fun saveAsync(dashboard: Dashboard): Deferred<Dashboard> = GlobalScope.async(IO) {
        log.debug("Saving dashboard $dashboard")
        transaction {
            if (dashboard.id == null) {
                DashboardDao.new {
                    name = dashboard.name
                    description = dashboard.description
                }
            } else {
                DashboardDao[dashboard.id!!].apply {
                    name = dashboard.name
                    description = dashboard.description
                    archived = dashboard.archived
                }
            }.toDto()
        }
    }

    fun addHealthcheckAsync(dashboardId: Long, hcId: Long): Deferred<Dashboard> = GlobalScope.async(IO) {
        transaction {
            val hc = HealthCheckEndpointDao.findById(hcId)!!
            DashboardDao.findById(dashboardId)!!.apply {
                healthchecks = SizedCollection(healthchecks.plus(hc))
            }.toDto()
        }
    }

    fun removeHealthcheckAsync(dashboardId: Long, hcId: Long): Deferred<Dashboard> = GlobalScope.async(IO) {
        transaction {
            DashboardDao.findById(dashboardId)!!.apply {
                healthchecks = SizedCollection(healthchecks.filterNot { it.id.value == hcId })
            }.toDto()
        }
    }

    fun archiveAsync(dashboardId: Long): Deferred<Dashboard> = GlobalScope.async(IO) {
        transaction {
            DashboardDao.findById(dashboardId)!!.apply {
                archived = true
            }.toDto()
        }
    }
}
