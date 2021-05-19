package com.shogo82148.ribbonizer.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import com.shogo82148.ribbonizer.FilterBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.File
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
        names.forEach { info(it) }
        info("task finished in " + (System.currentTimeMillis() - t0) + "ms")
    }

    private fun info(message: String) {
        project.logger.info("[$name] $message")
    }

    private val launcherIconNames: Set<String>
        get() {
            val names = HashSet<String>()
            androidManifestFiles.forEach { manifestFile: File ->
                try {
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
                "name",
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