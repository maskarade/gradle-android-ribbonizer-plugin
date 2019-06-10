package com.shogo82148.ribbonizer.plugin;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.api.AndroidSourceSet;
import com.android.build.gradle.api.ApplicationVariant;
import com.shogo82148.ribbonizer.FilterBuilder;
import com.shogo82148.ribbonizer.resource.ImageAdaptiveIcon;
import com.shogo82148.ribbonizer.resource.ImageIcon;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@SuppressWarnings({"WeakerAccess", "unused"})
public class RibbonizerTask extends DefaultTask {
    @TaskAction
    public void run() {
        if (filterBuilders.size() == 0) {
            return;
        }

        final long t0 = System.currentTimeMillis();

        final HashSet<String> names = new HashSet<>(iconNames);
        names.addAll(getLauncherIconNames());

        variant.getSourceSets().stream().flatMap(
                sourceProvider -> sourceProvider.getResDirectories().stream()
        ).forEach( resDir -> {
            if (resDir.equals(getOutputDir())) {
                return;
            }
            names.forEach(name -> getProject().fileTree(new LinkedHashMap<String, Object>() {
                {
                    put("dir", resDir);
                    put("include", Resources.resourceFilePattern(name));
                }
            }).forEach(inputFile -> {
                info("process " + inputFile);
                if (inputFile.getName().endsWith(".xml")) {
                    // assume it is an adaptive icon
                    processAdaptiveIcon(inputFile);
                } else {
                    processImageIcon(inputFile);
                }
            }));
        });

        info("task finished in " + (System.currentTimeMillis() - t0) + "ms");
    }

    private void processAdaptiveIcon(File inputFile) {
        try {
            final String icon = Resources.getAdaptiveIconResource(inputFile);
            if (icon.isEmpty()) {
                return;
            }
            variant.getSourceSets().stream().flatMap(
                    sourceProvider -> sourceProvider.getResDirectories().stream()
            ).forEach( resDir -> {
                if (resDir.equals(getOutputDir())) {
                    return;
                }
                getProject().fileTree(new LinkedHashMap<String, Object>() {
                    {
                        put("dir", resDir);
                        put("include", Resources.resourceFilePattern(icon));
                        put("exclude", "**/*.xml");
                    }
                }).forEach(this::processImageAdaptiveIcon);
            });
        } catch (Exception e) {
            info("Exception: " + e);
        }
    }

    private void processImageAdaptiveIcon(File inputFile) {
        final String basename = inputFile.getName();
        final String resType = inputFile.getParentFile().getName();
        File outputFile = new File(getOutputDir(), resType + "/" + basename);
        //noinspection ResultOfMethodCallIgnored
        outputFile.getParentFile().mkdirs();

        try {
            ImageAdaptiveIcon icon = new ImageAdaptiveIcon(inputFile);
            Ribbonizer ribbonizer = new Ribbonizer(icon, outputFile);
            ribbonizer.process(getFilterBuilders().stream().map(
                    filterBuilder -> filterBuilder.apply(getVariant(), inputFile)
            ));
            ribbonizer.save();
        } catch (Exception e) {
            info("Exception: " + e);
        }
    }

    private void processImageIcon(File inputFile) {
        final String basename = inputFile.getName();
        final String resType = inputFile.getParentFile().getName();
        File outputFile = new File(getOutputDir(), resType + "/" + basename);
        //noinspection ResultOfMethodCallIgnored
        outputFile.getParentFile().mkdirs();

        try {
            ImageIcon icon = new ImageIcon(inputFile);
            Ribbonizer ribbonizer = new Ribbonizer(icon, outputFile);
            ribbonizer.process(getFilterBuilders().stream().map(
                    filterBuilder -> filterBuilder.apply(getVariant(), inputFile)
            ));
            ribbonizer.save();
        } catch (Exception e) {
            info("Exception: " + e);
        }
    }

    public void info(String message) {
        //System.out.println("[$name] $message")
        getProject().getLogger().info("[" + getName() + "] " + message);
    }

    public Set<String> getLauncherIconNames() {
        final HashSet<String> names = new HashSet<>();
        getAndroidManifestFiles().forEach(manifestFile -> {
            try {
                names.addAll(Resources.getLauncherIcons(manifestFile));
            } catch (Exception e) {
                info("Exception: " + e);
            }
        });
        return names;
    }

    public Stream<File> getAndroidManifestFiles() {
        final AppExtension android = getProject().getExtensions().findByType(AppExtension.class);

        return new ArrayList<>(
                Arrays.asList("main", variant.getName(), variant.getBuildType().getName(), variant.getFlavorName())
        ).stream().filter(
                name -> !name.isEmpty()
        ).distinct().map( name -> {
            if (android == null) return null;
            AndroidSourceSet fileSet = android.getSourceSets().findByName(name);
            if (fileSet == null) return null;
            return getProject().file(fileSet.getManifest().getSrcFile());
        }).filter( manifestFile -> manifestFile != null && manifestFile.exists() );
    }

    public static String getNAME() {
        return NAME;
    }

    public ApplicationVariant getVariant() {
        return variant;
    }

    public void setVariant(ApplicationVariant variant) {
        this.variant = variant;
    }

    public File getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }

    public Set<String> getIconNames() {
        return iconNames;
    }

    public void setIconNames(Set<String> iconNames) {
        this.iconNames = iconNames;
    }

    public List<FilterBuilder> getFilterBuilders() {
        return filterBuilders;
    }

    public void setFilterBuilders(List<FilterBuilder> filterBuilders) {
        this.filterBuilders = filterBuilders;
    }

    private static final String NAME = "ribbonize";
    private ApplicationVariant variant;
    private File outputDir;
    private Set<String> iconNames;
    private List<FilterBuilder> filterBuilders = new ArrayList<>();
}
