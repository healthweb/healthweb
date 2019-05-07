package se.jensim.shared.models

import me.ntrrgc.tsGenerator.TypeScriptGenerator
import me.ntrrgc.tsGenerator.VoidType
import org.junit.Assert
import org.junit.Test
import java.io.File

class GenerateTypescript {

    private val dirName = "build/ts"
    private val fileName = "shared-types.d.ts"

    @Test
    fun name() {
        val newFile = getFileRef()

        val tsText = TypeScriptGenerator(
            rootClasses = setOf(HealthCheckEndpoint::class),
            intTypeName = "int",
            voidType = VoidType.UNDEFINED

        ).definitionsText
            .split("\n")
            .joinToString("\n") {
                it.replace(Regex("^(interface|type) "), "export $1 ")
            }

        newFile.writeText(tsText)

        Assert.assertTrue(newFile.readText().isNotEmpty())
    }

    fun getFileRef(): File {
        val rootUrl = GenerateTypescript::class.java.classLoader.getResource("/")
        val dir: File = if (rootUrl == null) {
            File(dirName)
        } else {
            File(File(rootUrl.toURI()), dirName)
        }
        dir.mkdirs()

        return File(dir, fileName)
    }
}
