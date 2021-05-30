package com.shogo82148.ribbonizer.plugin

import com.android.build.gradle.AppExtension
import com.android.builder.model.SourceProvider
import com.android.build.gradle.api.ApplicationVariant
import com.shogo82148.ribbonizer.FilterBuilder
import com.shogo82148.ribbonizer.plugin.Resources.adaptiveIconResource
import com.shogo82148.ribbonizer.resource.ImageAdaptiveIcon
import com.shogo82148.ribbonizer.resource.ImageIcon
import com.shogo82148.ribbonizer.resource.Resource
import com.shogo82148.ribbonizer.resource.VectorAdaptiveIcon
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
        names.forEach { name: String ->
            findResourceFiles(name).forEach {
                info("process $it")
                if (it.name.endsWith(".xml")) {
                    // assume it is an adaptive icon
                    processAdaptiveIcon(it)
                } else {
                    processIcon(it, ImageIcon(it))
                }
            }
        }
        info("task finished in " + (System.currentTimeMillis() - t0) + "ms")
    }

    private fun findResourceFiles(name: String): List<File> {
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

    private fun processAdaptiveIcon(inputFile: File) {
        try {
            val icon = adaptiveIconResource(inputFile)
            if (icon.isEmpty()) {
                return
            }
            findResourceFiles(icon).forEach {
                if (it.name.endsWith(".xml")) {
                    // assume it is a vector drawable
                    processIcon(it, VectorAdaptiveIcon(it))
                } else {
                    processIcon(it, ImageAdaptiveIcon(it))
                }
            }
        } catch (e: Exception) {
            info("Exception: $e")
        }
    }

    private fun <T: Resource> processIcon(inputFile: File, icon: T) {
        val basename = inputFile.name
        val resType = inputFile.parentFile.name
        val outputFile = File(outputDir, "$resType/$basename")
        try {
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
                it?.exists() ?: false
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