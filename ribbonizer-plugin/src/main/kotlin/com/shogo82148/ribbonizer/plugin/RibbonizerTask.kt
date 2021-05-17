package com.shogo82148.ribbonizer.plugin

import com.android.build.gradle.api.ApplicationVariant
import com.shogo82148.ribbonizer.FilterBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.File

open class RibbonizerTask : DefaultTask() {
    @TaskAction
    fun run() {
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