package com.shogo82148.ribbonizer.plugin

import com.shogo82148.ribbonizer.FilterBuilder
import com.shogo82148.ribbonizer.resource.Resource
import com.shogo82148.ribbonizer.resource.Variant
import org.gradle.api.Project
import java.io.File

class Ribbonizer (
    private val name: String,
    private val project: Project,
    private val outputDir: File,
    private val variant: Variant,
    private val filterBuilders: List<FilterBuilder>
) {
    fun process(resource: Resource) {
        try {
            val file = resource.file
            info("process $file")

            val basename = file.name
            val resType = file.parentFile.name
            val outputFile = File(outputDir, "$resType/$basename")
            outputFile.parentFile.mkdirs()
            filterBuilders.forEach { filterBuilder: FilterBuilder ->
                val filter = filterBuilder.apply(variant, file)
                filter?.accept(resource)
            }
            info("saving to $outputFile")
            resource.save(outputFile)
        } catch (e: Exception) {
            info("Exception: $e")
        }
    }

    fun info(message: String) {
        project.logger.info("[$name] $message")
    }
}