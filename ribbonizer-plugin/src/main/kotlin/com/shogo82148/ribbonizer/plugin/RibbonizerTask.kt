package com.shogo82148.ribbonizer.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import com.shogo82148.ribbonizer.FilterBuilder
import com.shogo82148.ribbonizer.resource.AdaptiveIcon
import com.shogo82148.ribbonizer.resource.ImageIcon
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.stream.Stream

abstract class RibbonizerTask : DefaultTask() {
    @ExperimentalStdlibApi
    @TaskAction
    fun run() {
//        if (filterBuilders.isEmpty()) {
//            return
//        }
//        val t0 = System.currentTimeMillis()
//        val names = HashSet(iconNames)
//        names.addAll(launcherIconNames)
//        info(names.toString())
//        val ribbonizer = Ribbonizer(name, project, variant, outputDir, filterBuilders)
//        names.forEach { name: String ->
//            ribbonizer.findResourceFiles(name).forEach {
//                when (it.extension) {
//                    "xml" -> {
//                        val icon = AdaptiveIcon(ribbonizer, it)
//                        ribbonizer.process(icon)
//                    }
//                    "png" -> {
//                        val icon = ImageIcon(ribbonizer, it)
//                        ribbonizer.process(icon)
//                    }
//                }
//            }
//        }
//        info("task finished in " + (System.currentTimeMillis() - t0) + "ms")
    }

    private fun info(message: String) {
        project.logger.info("[$name] $message")
    }

//    private val launcherIconNames: Set<String>
//        get() {
//            val names = HashSet<String>()
//            androidManifestFiles.forEach { manifestFile: File ->
//                try {
//                    info("manifestFile: " + manifestFile.name)
//                    names.addAll(Resources.launcherIcons(manifestFile))
//                } catch (e: Exception) {
//                    info("Exception: $e")
//                }
//            }
//            return names
//        }

//    private val androidManifestFiles: Stream<File>
//        get() {
//            val android = project.extensions.findByType(
//                AppExtension::class.java
//            )
//            return listOf<String>(
//                "main",
//                variant.name,
//                variant.buildType.name,
//                variant.flavorName
//            ).stream().filter {
//                it.isNotEmpty()
//            }.distinct().map {
//                val fileSet = android?.sourceSets?.findByName(it) ?: return@map null
//                project.file(fileSet.manifest.srcFile)
//            }.filter {
//                it?.exists() ?: false
//            }.map { it!! }
//        }

    @get:OutputFile
    abstract val outputDir: DirectoryProperty

    companion object {
        const val NAME = "ribbonize"
    }
}