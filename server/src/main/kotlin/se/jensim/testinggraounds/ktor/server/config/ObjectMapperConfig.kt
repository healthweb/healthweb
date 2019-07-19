package se.jensim.testinggraounds.ktor.server.config

import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.concurrent.atomic.AtomicReference

object ObjectMapperConfig {

    private val ob: AtomicReference<ObjectMapper> = AtomicReference()

    fun config(objectMapper: ObjectMapper? = null): ObjectMapper =
        if (objectMapper != null) {
            ob.compareAndSet(null, objectMapper)
            objectMapper
        } else {
            val mapper = jacksonObjectMapper()
            ob.compareAndSet(null, mapper)
            mapper
        }.run {
            configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            configure(DeserializationFeature.FAIL_ON_TRAILING_TOKENS, true)
            configure(SerializationFeature.INDENT_OUTPUT, true)
            setDefaultPrettyPrinter(DefaultPrettyPrinter().apply {
                indentArraysWith(DefaultPrettyPrinter.FixedSpaceIndenter.instance)
                indentObjectsWith(DefaultIndenter("  ", "\n"))
            })
            registerModule(JavaTimeModule())  // support java.time.* types
        }

}
