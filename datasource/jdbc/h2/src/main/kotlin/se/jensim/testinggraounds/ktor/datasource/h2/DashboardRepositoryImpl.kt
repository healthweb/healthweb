package se.jensim.testinggraounds.ktor.datasource.h2

import se.jensim.testinggraounds.ktor.datasource.api.HealthwebRepository
import se.jensim.testinggraounds.ktor.shared.Dashboard

class DashboardRepositoryImpl : HealthwebRepository<Dashboard, Long> {
    override fun getById(id: Long): Dashboard {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAll(): Collection<Dashboard> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun save(item: Dashboard): Dashboard {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
