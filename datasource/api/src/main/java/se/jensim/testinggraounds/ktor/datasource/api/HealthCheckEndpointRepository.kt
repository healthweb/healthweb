package se.jensim.testinggraounds.ktor.datasource.api

import se.jensim.testinggraounds.ktor.shared.HealthCheckEndpoint

interface HealthCheckEndpointRepository : HealthwebRepository<HealthCheckEndpoint, Long>
