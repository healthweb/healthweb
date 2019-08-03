package com.github.healthweb.server.dashboard

import com.github.healthweb.server.extensions.toDto
import com.github.healthweb.server.healthcheck.HealthCheckEndpointTable
import com.github.healthweb.shared.Dashboard
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class DashboardService {

    companion object {
        val singleton = DashboardService()
        private val log = LoggerFactory.getLogger(DashboardService::class.java)
    }

    fun getAll(): List<Dashboard> {
        log.debug("Get all dashboards")
        return transaction { DashboardDao.all().toList() }
                .map { it.toDto() }
    }

    fun saveAsync(dashboard: Dashboard): Deferred<Dashboard> = GlobalScope.async(IO) {
        log.debug("Saving dashboard $dashboard")
        if (dashboard.id == null) {
            transaction {
                DashboardDao.new {
                    name = dashboard.name
                    description = dashboard.description
                }
            }.toDto()
        } else {
            transaction {
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
            HealthcheckDashboardTable.insert {
                it[healthcheck] = EntityID(hcId, HealthCheckEndpointTable)
                it[dashboard] = EntityID(dashboardId, DashboardTable)
            }
        }
        transaction {
            DashboardDao.findById(dashboardId)
        }!!.toDto()
    }

    fun removeHealthcheckAsync(dashboardId: Long, hcId: Long): Deferred<Dashboard> = GlobalScope.async(IO) {
        transaction {
            HealthcheckDashboardTable.deleteWhere {
                HealthcheckDashboardTable.healthcheck.eq(hcId) and HealthcheckDashboardTable.dashboard.eq(dashboardId)
            }
        }
        transaction {
            DashboardDao.findById(dashboardId)
        }!!.toDto()
    }

    fun archiveAsync(dashboardId: Long): Deferred<Dashboard> = GlobalScope.async(IO) {
        transaction {
            DashboardDao.findById(dashboardId)?.apply {
                archived = true
            }
        }!!.toDto()
    }
}
