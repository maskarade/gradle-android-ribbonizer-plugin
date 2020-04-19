package com.shogo82148.ribbonizer.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import java.io.File

class RibbonizerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.add(RibbonizerExtension.NAME, RibbonizerExtension)

        project.afterEvaluate {
            val android = project.extensions.findByType(AppExtension::class.java)
                ?: throw Exception("Not an Android application; you forget `apply plugin: 'com.android.application`?")
            val extension = project.extensions.findByType(RibbonizerExtension::class.java)!!

            val tasks = ArrayList<Task>()

            android.applicationVariants.all { variant ->
                if ((!variant.buildType.isDebuggable) &&
                    (!extension.forcedVariantsNames.contains(variant.name))
                ) {
                    project.logger.info("[ribbonizer] skip ${variant.name} because it is not debuggable and not forced.")
                    return@all
                }

                var filterBuilders = extension.filterBuilders

                val generatedResDir = getGeneratedResDir(project, variant)
                android.sourceSets.findByName(variant.name)!!.res.srcDir(generatedResDir)

                val name = "${RibbonizerTask.NAME}${capitalize(variant.name)}"
                val task = project.task(
                    mapOf("type" to RibbonizerTask::class.java),
                    name
                ) as RibbonizerTask
                task.variant = variant
                task.outputDir = generatedResDir
                task.iconNames = HashSet(extension.iconNames)
                task.filterBuilders = filterBuilders
                tasks.add(task)

                val generatedResources =
                    project.getTasksByName("generate${capitalize(variant.name)}Resources", false)
                generatedResources.forEach {
                    it.dependsOn(task)
                }
            }

            project.task(mapOf("dependsOn" to tasks), RibbonizerTask.NAME)
        }
    }
}

fun capitalize(string: String): String {
    return string.substring(0, 1).toUpperCase() + string.substring(1)
}

fun getGeneratedResDir(project: Project, variant: ApplicationVariant): File {
    return File(project.buildDir, "generated/ribbonizer/res/${variant.name}")
}
