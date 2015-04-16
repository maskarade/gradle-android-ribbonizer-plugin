package com.github.gfx.ribbonizer.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import com.android.builder.model.SourceProvider
import com.github.gfx.ribbonizer.FilterBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.awt.image.BufferedImage
import java.util.function.Consumer
import java.util.function.Function
import java.util.stream.Stream

class RibbonizerTask extends DefaultTask {

    static final String NAME = "ribbonize"

    ApplicationVariant variant

    //@OutputDirectory
    File outputDir

    // `iconNames` includes: "@drawable/icon", "@mipmap/ic_launcher", etc.
    Set<String> iconNames

    List<FilterBuilder> filterBuilders = []

    @TaskAction
    public void run() {
        if (filterBuilders.size() == 0) {
            return;
        }

        def t0 = System.currentTimeMillis()

        def names = new HashSet<String>(iconNames)
        names.addAll(launcherIconNames)

        variant.sourceSets.stream()
                .flatMap(new Function<SourceProvider, Stream>() {

            @Override
            Stream apply(SourceProvider sourceProvider) {
                return sourceProvider.resDirectories.stream()
            }
        }).forEach { File resDir ->
            if (resDir == outputDir) {
                return
            }

            names.forEach { String name ->
                project.fileTree(
                        dir: resDir,
                        include: Resources.resourceFilePattern(name),
                ).forEach { File inputFile ->
                    info "process $inputFile"

                    def basename = inputFile.name
                    def resType = inputFile.parentFile.name
                    def outputFile = new File(outputDir, "${resType}/${basename}")
                    outputFile.parentFile.mkdirs()

                    def ribbonizer = new Ribbonizer(inputFile, outputFile)
                    ribbonizer.process(filterBuilders.stream()
                            .map(new Function<FilterBuilder, Consumer<BufferedImage>>() {

                        @Override
                        Consumer<BufferedImage> apply(FilterBuilder filterBuilder) {
                            return filterBuilder.apply(variant, inputFile)
                        }
                    }))
                    ribbonizer.save()
                }
            }
        }

        info("task finished in ${System.currentTimeMillis() - t0}ms")
    }

    void info(String message) {
        //System.out.println("[$name] $message")
        project.logger.info("[$name] $message")
    }

    Set<String> getLauncherIconNames() {
        def names = new HashSet<String>()
        androidManifestFiles.forEach { File manifestFile ->
            names.add(Resources.getLauncherIcon(manifestFile))
        }
        return names
    }

    Stream<File> getAndroidManifestFiles() {
        AppExtension android = project.extensions.findByType(AppExtension)

        return ["main", variant.name, variant.buildType.name, variant.flavorName].stream()
                .filter({ name -> !name.empty })
                .distinct()
                .map({ name -> project.file(android.sourceSets[name].manifest.srcFile) })
                .filter({ manifestFile -> manifestFile.exists() })
    }
}