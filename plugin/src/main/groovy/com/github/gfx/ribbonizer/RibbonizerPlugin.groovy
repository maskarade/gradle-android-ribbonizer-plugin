package com.github.gfx.ribbonizer
import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

// see http://www.gradle.org/docs/current/userguide/custom_plugins.html

@CompileStatic
public class RibbonizerPlugin implements Plugin<Project> {
    static {
        System.setProperty("java.awt.headless", "true")
    }

    @Override
    void apply(Project project) {
        project.extensions.add(RibbonizerExtension.NAME, RibbonizerExtension)

        project.afterEvaluate {
            def android = project.extensions.findByType(AppExtension)

            def tasks = new ArrayList<Task>();

            android.applicationVariants.all { ApplicationVariant variant ->
                if (!variant.buildType.debuggable) {
                    project.logger.info("[ribbonizer] skip ${variant.name} because it is not debuggable.")
                    return;
                }

                def generatedResDir = new File(project.buildDir, "generated/ribbonizer/res/${variant.name}")
                android.sourceSets.findByName(variant.name).res.srcDir(generatedResDir)

                def name = "${RibbonizerTask.NAME}${capitalize(variant.name)}"
                def task = project.task(name, type: RibbonizerTask) as RibbonizerTask
                task.variant = variant
                task.outputDir = generatedResDir
                tasks.add(task)

                def generateResources = project.getTasksByName("generate${capitalize(variant.name)}Resources", false)
                generateResources.forEach { Task t ->
                    t.dependsOn(task)
                }
            }

            project.task(RibbonizerTask.NAME, dependsOn: tasks);
        }
    }

    static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
