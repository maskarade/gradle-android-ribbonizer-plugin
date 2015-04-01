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

        // workaround for an Android Studio issue
        try {
            Class.forName(System.getProperty("java.awt.graphicsenv"))
        } catch (ClassNotFoundException e) {
            System.err.println("[WARN] java.awt.graphicsenv: " + e)
            System.setProperty("java.awt.graphicsenv", "sun.awt.CGraphicsEnvironment")
        }
        try {
            Class.forName(System.getProperty("awt.toolkit"))
        } catch (ClassNotFoundException e) {
            System.err.println("[WARN] awt.toolkit: " + e)
            System.setProperty("awt.toolkit", "sun.lwawt.macosx.LWCToolkit")
        }
    }

    @Override
    void apply(Project project) {
        project.extensions.add(RibbonizerExtension.NAME, RibbonizerExtension)

        project.afterEvaluate {
            def android = project.extensions.findByType(AppExtension)
            if (!android) {
                throw new Exception("Not an Android application; you forget `apply plugin: 'com.android.application`?")
            }
            def extension = project.extensions.findByType(RibbonizerExtension)

            def tasks = new ArrayList<Task>();

            android.applicationVariants.all { ApplicationVariant variant ->
                if (!variant.buildType.debuggable) {
                    project.logger.
                            info("[ribbonizer] skip ${variant.name} because it is not debuggable.")
                    return;
                }
                def generatedResDir = new File(project.buildDir,
                        "generated/ribbonizer/res/${variant.name}")
                android.sourceSets.findByName(variant.name).res.srcDir(generatedResDir)

                def name = "${RibbonizerTask.NAME}${capitalize(variant.name)}"
                def task = project.task(name, type: RibbonizerTask) as RibbonizerTask
                task.variant = variant
                task.outputDir = generatedResDir
                for (FilterBuilder builder : extension.filterBuilders) {
                    task.filters.add(builder.apply(variant))
                }
                tasks.add(task)

                def generateResources = project.
                        getTasksByName("generate${capitalize(variant.name)}Resources", false)
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
