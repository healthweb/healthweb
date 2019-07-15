package se.jensim.testinggraounds.ktor.server.config

import com.mongodb.client.model.IndexOptions
import kotlinx.coroutines.runBlocking
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import se.jensim.testinggraounds.ktor.server.config.PropertiesConfig.mongoUrl
import se.jensim.testinggraounds.ktor.server.healthcheck.HealthCheckEndpoint

object MongoConfig {

    private val client by lazy { KMongo.createClient(mongoUrl()).coroutine }
    val database by lazy { client.getDatabase("dropwizard_dashboard") }

    val healthCheckEndpointCollection by lazy {
        database.getCollection<HealthCheckEndpoint>("healthCheckEndpoint").apply {
            runBlocking {
                createIndex("{url: 1}", IndexOptions().apply { unique(true) })
            }
        }
    }
}
