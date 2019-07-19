package se.jensim.testinggraounds.ktor.datasource.api

interface HealthwebRepository<T, ID> {

    fun getById(id: ID): T
    fun getAll(): Collection<T>
    fun save(item: T):T
}
