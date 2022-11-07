package com.shogo82148.ribbonizer.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class RibbonizerTask : DefaultTask() {
    @ExperimentalStdlibApi
    @TaskAction
    fun run() {
//        if (filterBuilders.isEmpty()) {
//            return
//        }
        val t0 = System.currentTimeMillis()
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
        iconFiles.get().forEach {
            info(it.path)
        }
        outputDir.get().asFile.mkdirs()
        File(outputDir.get().asFile, "custom_asset.txt")
            .writeText("some real asset file")
        info(outputDir.get().asFile.path)
        info("task finished in " + (System.currentTimeMillis() - t0) + "ms")
    }

    private fun info(message: String) {
        project.logger.info("[$name] \uD83C\uDF80 $message")
    }

    @get:InputFiles
    abstract val iconFiles: ListProperty<File>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    companion object {
        const val NAME = "ribbonize"
    }
}