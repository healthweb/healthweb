package se.jensim.testinggraounds.ktor.datasource.h2

import se.jensim.testinggraounds.ktor.datasource.api.HealthwebRepository
import se.jensim.testinggraounds.ktor.shared.HealthCheckEndpoint

class HealthCheckEndpointRepositoryImpl : HealthwebRepository<HealthCheckEndpoint, Long> {
    override fun getById(id: Long): HealthCheckEndpoint {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAll(): Collection<HealthCheckEndpoint> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun save(item: HealthCheckEndpoint): HealthCheckEndpoint {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
