package se.jensim.testinggraounds.ktor.server.config

import kotlinx.coroutines.runBlocking
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.coroutine
import java.util.*

object TestMongoConfig {

    fun <T : Any> tempCollection(
        clazz: Class<T>,
        collectionName: String? = null,
        action: suspend (CoroutineCollection<T>) -> Unit
    ) {
        runBlocking {
            val col: CoroutineCollection<T> =
                MongoConfig.database.database.getCollection(collectionName ?: UUID.randomUUID().toString(), clazz)
                    .coroutine
            try {
                action(col)
            } finally {
                col.drop()
            }
        }
    }
}
