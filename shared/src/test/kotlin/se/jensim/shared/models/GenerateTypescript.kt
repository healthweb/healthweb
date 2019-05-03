package se.jensim.shared.models

import me.ntrrgc.tsGenerator.TypeScriptGenerator
import me.ntrrgc.tsGenerator.VoidType
import org.junit.Assert
import org.junit.Test
import java.io.File

class GenerateTypescript {

    val dir = File(GenerateTypescript::class.java.getResource("/").toURI())
    val newFile = File(dir, "shared-types.d.ts")

    @Test
    fun name() {
        if (newFile.exists()) {
            newFile.delete()
        }
        newFile.createNewFile()
        val tsText = TypeScriptGenerator(
            rootClasses = setOf(HealthCheckEndpoint::class),
            intTypeName = "int",
            voidType = VoidType.UNDEFINED

        ).definitionsText
            .replace(Regex("interface "), "export interface ")

        val stream = newFile.outputStream()
        stream.write(tsText.toByteArray())
        stream.close()

        Assert.assertTrue(newFile.readText().isNotEmpty())
    }
}
