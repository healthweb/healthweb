package se.jensim.testinggraounds.ktor.datasource.api

import se.jensim.testinggraounds.ktor.shared.Dashboard

interface DashboardRepository : HealthwebRepository<Dashboard, Long>
