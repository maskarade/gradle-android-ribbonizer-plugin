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
                def name = "${RibbonizerTask.NAME}${capitalize(variant.name)}"
                def task = project.task(name, type: RibbonizerTask) as RibbonizerTask
                task.variant = variant
                tasks.add(task)

                project.getTasksByName("process${capitalize(variant.name)}Resources", false).forEach {
                    Task t ->
                        t << {
                            task.run()
                        }
                }
            }

            project.task(RibbonizerTask.NAME, dependsOn: tasks);
        }
    }

    static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
