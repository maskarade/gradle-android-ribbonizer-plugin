package com.github.gfx.ribbonizer

import com.android.build.gradle.api.ApplicationVariant
import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

@CompileStatic
class RibbonizerTask extends DefaultTask {
    static final String NAME = "ribbonize"

    ApplicationVariant variant

    @TaskAction
    public void run() {
        System.out.println(variant.name)

        def extension = project.extensions.getByType(RibbonizerExtension)
        System.out.println(extension.message)
    }

}