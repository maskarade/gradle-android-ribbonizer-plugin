package com.github.gfx.ribbonizer.plugin
import com.android.build.gradle.api.ApplicationVariant
import com.android.builder.model.SourceProvider
import groovy.transform.CompileStatic
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.awt.image.BufferedImage
import java.util.function.Consumer

@CompileStatic
class RibbonizerTask extends DefaultTask {
    static final String NAME = "ribbonize"

    ApplicationVariant variant

    //@OutputDirectory
    File outputDir

    List<Consumer<BufferedImage>> filters = []

    @TaskAction
    public void run() {
        if (filters.size() == 0) {
            return;
        }

        def t0 = System.currentTimeMillis()

        def extension = project.extensions.getByType(RibbonizerExtension)

        variant.sourceSets.forEach { SourceProvider sourceSet ->
            sourceSet.resDirectories.forEach { File resDir ->
                if (resDir == outputDir) {
                    return
                }

                extension.iconNames.forEach { String name ->
                    info "process $name in $resDir"

                    project.fileTree(
                            dir: resDir,
                            includes: [
                                    "drawable*/${name}.png",
                                    "mipmap*/${name}.png",
                            ]
                    ).forEach { File inputFile ->
                        def basename = inputFile.name
                        def resType = inputFile.parentFile.name
                        def outputFile = new File(outputDir, "${resType}/${basename}")
                        outputFile.parentFile.mkdirs()

                        def ribbonizer = new Ribbonizer(inputFile, outputFile)
                        ribbonizer.process(filters)
                        ribbonizer.save()
                    }
                }
            }
        }

        info("${this.name} ${System.currentTimeMillis() - t0}ms")
    }

    public void info(String message) {
        project.logger.info("[$name] $message")
    }
}