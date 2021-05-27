package com.shogo82148.ribbonizer.plugin

import com.android.build.gradle.AppExtension
import com.android.builder.model.SourceProvider
import com.android.build.gradle.api.ApplicationVariant
import com.shogo82148.ribbonizer.FilterBuilder
import com.shogo82148.ribbonizer.plugin.Resources.resourceFilePattern
import com.shogo82148.ribbonizer.resource.ImageIcon
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.function.Consumer
import java.util.stream.Stream

open class RibbonizerTask : DefaultTask() {
    @TaskAction
    fun run() {
        if (filterBuilders.isEmpty()) {
            return
        }
        val t0 = System.currentTimeMillis()
        val names = HashSet(iconNames)
        names.addAll(launcherIconNames)
        info(names.toString())
        variant.sourceSets.stream().flatMap { sourceProvider: SourceProvider ->
            sourceProvider.resDirectories.stream()
        }.forEach { resDir: File ->
            if (resDir == outputDir) {
                return@forEach
            }
            names.forEach(Consumer { name: String? ->
                project.fileTree(object :
                    LinkedHashMap<String?, Any?>() {
                    init {
                        put("dir", resDir)
                        put("include", resourceFilePattern(name!!))
                    }
                }).forEach(Consumer { inputFile: File ->
                    info("process $inputFile")
                    if (inputFile.name.endsWith(".xml")) {
                        // assume it is an adaptive icon
                        // TODO: processAdaptiveIcon(inputFile)
                    } else {
                        processImageIcon(inputFile)
                    }
                })
            })
        }
        info("task finished in " + (System.currentTimeMillis() - t0) + "ms")
    }

    private fun processImageIcon(inputFile: File) {
        val basename = inputFile.name
        val resType = inputFile.parentFile.name
        val outputFile = File(outputDir, "$resType/$basename")
        outputFile.parentFile.mkdirs()
        try {
            val icon = ImageIcon(inputFile)
            val ribbonizer = Ribbonizer(icon, outputFile)
            ribbonizer.process(
                filterBuilders.stream().map { filterBuilder: FilterBuilder ->
                    filterBuilder.apply(
                        variant,
                        inputFile
                    )
                }
            )
            ribbonizer.save()
        } catch (e: Exception) {
            info("Exception: $e")
        }
    }

    private fun info(message: String) {
        project.logger.info("[$name] $message")
    }

    private val launcherIconNames: Set<String>
        get() {
            val names = HashSet<String>()
            androidManifestFiles.forEach { manifestFile: File ->
                try {
                    info("manifestFile: " + manifestFile.name)
                    names.addAll(Resources.launcherIcons(manifestFile))
                } catch (e: Exception) {
                    info("Exception: $e")
                }
            }
            return names
        }

    private val androidManifestFiles: Stream<File>
        get() {
            val android = project.extensions.findByType(
                AppExtension::class.java
            )
            return listOf<String>(
                "main",
                variant.name,
                variant.buildType.name,
                variant.flavorName
            ).stream().filter {
                it.isNotEmpty()
            }.distinct().map {
                val fileSet = android?.sourceSets?.findByName(it) ?: return@map null
                project.file(fileSet.manifest.srcFile)
            }.filter {
                it != null && it.exists()
            }.map { it!! }
        }

    @Internal
    lateinit var variant: ApplicationVariant

    @Internal
    lateinit var outputDir: File

    @Internal
    lateinit var iconNames: Set<String>

    @Internal
    lateinit var filterBuilders: List<FilterBuilder>

    companion object {
        const val NAME = "ribbonize"
    }
}