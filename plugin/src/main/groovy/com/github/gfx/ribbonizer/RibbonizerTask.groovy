package com.github.gfx.ribbonizer
import com.android.build.gradle.api.ApplicationVariant
import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

@CompileStatic
class RibbonizerTask extends DefaultTask {
    static final String NAME = "ribbonize"

    ApplicationVariant variant

    List<Ribbonizer.Filter> filters = [new GrayScaleFilter() as Ribbonizer.Filter]

    @TaskAction
    public void run() {
        def extension = project.extensions.getByType(RibbonizerExtension)

        extension.iconNames.forEach { String name ->
            // e.g. example/build/intermediates/res/local/debug/drawable-hdpi-v4/ic_launcher.png
            project.fileTree(
                    dir: project.buildDir,
                    includes: [
                            "intermediates/res/${variant.flavorName}/${variant.buildType.name}/drawable*/${name}.png",
                            "intermediates/res/${variant.flavorName}/${variant.buildType.name}/mipmap*/${name}.png",
                    ]
            ).forEach { File file ->
                def ribbonizer = new Ribbonizer(file, file)
                ribbonizer.process(filters)
                ribbonizer.save()
            }
        }
    }


}