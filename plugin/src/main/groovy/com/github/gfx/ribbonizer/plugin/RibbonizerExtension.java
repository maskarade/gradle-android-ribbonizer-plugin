package com.github.gfx.ribbonizer.plugin;

import com.android.build.gradle.api.ApplicationVariant;
import com.github.gfx.ribbonizer.CustomColorRibbonBuilder;
import com.github.gfx.ribbonizer.FilterBuilder;
import com.github.gfx.ribbonizer.GrayScaleBuilder;
import com.github.gfx.ribbonizer.GrayRibbonBuilder;
import com.github.gfx.ribbonizer.GreenRibbonBuilder;
import com.github.gfx.ribbonizer.YellowRibbonBuilder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

class RibbonizerExtension {

    public static String NAME = "ribbonizer";

    Set<String> forcedVariantsNames = new HashSet<>();

    Set<String> iconNames = new HashSet<>();

    List<FilterBuilder> filterBuilders = new ArrayList<>();

    public RibbonizerExtension() {
    }

    public Set<String> getIconNames() {
        return iconNames;
    }

    /**
     * @param resNames Names of icons. For example "@drawable/ic_launcher", "@mipmap/icon"
     */
    public void setIconNames(Collection<String> resNames) {
        iconNames = new HashSet<>(resNames);
    }

    /**
     * @param resNames Names of icons. For example "@drawable/ic_launcher", "@mipmap/icon"
     */
    public void iconNames(Collection<String> resNames) {
        setIconNames(resNames);
    }

    /**
     * @param resNames Names of icons. For example "@drawable/ic_launcher", "@mipmap/icon"
     */
    public void iconNames(String... resNames) {
        setIconNames(Arrays.asList(resNames));
    }

    /**
     * @param resName A name of icons. For example "@drawable/ic_launcher", "@mipmap/icon"
     */
    public void iconName(String resName) {
        iconNames.add(resName);
    }

    public Set<String> getForcedVariantsNames() {
        return forcedVariantsNames;
    }

    public void forcedVariantsNames(String... variantsNames) {
        forcedVariantsNames = new HashSet<>(Arrays.asList(variantsNames));
    }

    public List<FilterBuilder> getFilterBuilders() {
        return filterBuilders;
    }

    public void setFilterBuilders(Collection<FilterBuilder> filterBuilders) {
        this.filterBuilders = new ArrayList<>(filterBuilders);
    }

    public void builder(FilterBuilder filterBuilder)
            throws IllegalAccessException, InstantiationException {
        this.filterBuilders.clear();
        this.filterBuilders.add(filterBuilder);
    }

    // utilities

    public Consumer<BufferedImage> grayScaleFilter(ApplicationVariant variant, File iconFile) {
        return new GrayScaleBuilder().apply(variant, iconFile);
    }

    public Consumer<BufferedImage> grayRibbonFilter(ApplicationVariant variant, File iconFile) {
        return new GrayRibbonBuilder().apply(variant, iconFile);
    }

    public Consumer<BufferedImage> yellowRibbonFilter(ApplicationVariant variant, File iconFile) {
        return new YellowRibbonBuilder().apply(variant, iconFile);
    }

    public Consumer<BufferedImage> greenRibbonFilter(ApplicationVariant variant, File iconFile) {
        return new GreenRibbonBuilder().apply(variant, iconFile);
    }

    public Consumer<BufferedImage> customColorRibbonFilter(ApplicationVariant variant, File iconFile, String nm) {
        return new CustomColorRibbonBuilder(nm).apply(variant, iconFile);
    }
}
