package com.github.gfx.ribbonizer

import org.gradle.api.Plugin
import org.gradle.api.Project

// see http://www.gradle.org/docs/current/userguide/custom_plugins.html

public class RibbonizerPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.add(RibbonizerExtension.NAME, RibbonizerExtension)

        project.task(RibbonizeTask, type: RibbonizeTask)
    }
}
