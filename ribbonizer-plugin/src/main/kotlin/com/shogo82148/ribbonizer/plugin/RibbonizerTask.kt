package com.shogo82148.ribbonizer.plugin

import com.shogo82148.ribbonizer.FilterBuilder
import com.shogo82148.ribbonizer.resource.AdaptiveIcon
import com.shogo82148.ribbonizer.resource.ImageIcon
import com.shogo82148.ribbonizer.resource.Variant
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class RibbonizerTask : DefaultTask() {
    @ExperimentalStdlibApi
    @TaskAction
    fun run() {
        if (filterBuilders.get().isEmpty()) {
            return
        }
        val t0 = System.currentTimeMillis()
        outputDir.get().asFile.mkdirs()
        val ribbonizer = Ribbonizer(name, project, outputDir.get().asFile, iconFiles.get(), variant.get(), filterBuilders.get())
        ribbonizer.process()
        info("task finished in " + (System.currentTimeMillis() - t0) + "ms")
    }

    private fun info(message: String) {
        project.logger.info("[$name] \uD83C\uDF80 $message")
    }

    @get:InputFiles
    abstract val iconFiles: ListProperty<File>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @get:Internal
    abstract val filterBuilders: ListProperty<FilterBuilder>

    @get:Internal
    abstract val variant: Property<Variant>

    companion object {
        const val NAME = "ribbonize"
    }
}