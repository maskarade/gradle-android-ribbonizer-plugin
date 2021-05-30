package com.shogo82148.ribbonizer.plugin

import com.android.build.gradle.api.ApplicationVariant
import com.android.builder.model.SourceProvider
import com.shogo82148.ribbonizer.FilterBuilder
import com.shogo82148.ribbonizer.resource.Resource
import org.gradle.api.Project
import java.io.File
import java.util.function.Consumer

class Ribbonizer (
    private val name: String,
    private val project: Project,
    private val variant: ApplicationVariant,
    private val outputDir: File,
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

    fun findResourceFiles(name: String): List<File> {
        val files = ArrayList<File>()
        variant.sourceSets.stream().flatMap { sourceProvider: SourceProvider ->
            sourceProvider.resDirectories.stream()
        }.forEach { resDir: File ->
            if (resDir == outputDir) {
                return@forEach
            }
            project.fileTree(object :
                LinkedHashMap<String?, Any?>() {
                init {
                    put("dir", resDir)
                    put("include", Resources.resourceFilePattern(name))
                }
            }).forEach(Consumer { inputFile: File ->
                files.add(inputFile)
            })
        }
        return files
    }

    fun info(message: String) {
        project.logger.info("[$name] $message")
    }
}