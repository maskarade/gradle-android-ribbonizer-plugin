package com.shogo82148.ribbonizer.plugin;

import com.android.build.gradle.api.ApplicationVariant;
import com.shogo82148.ribbonizer.CustomColorRibbonBuilder;
import com.shogo82148.ribbonizer.FilterBuilder;
import com.shogo82148.ribbonizer.GrayScaleBuilder;
import com.shogo82148.ribbonizer.GrayRibbonBuilder;
import com.shogo82148.ribbonizer.GreenRibbonBuilder;
import com.shogo82148.ribbonizer.YellowRibbonBuilder;
import com.shogo82148.ribbonizer.filter.ColorRibbonFilter;
import com.shogo82148.ribbonizer.filter.GrayScaleFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings({"WeakerAccess", "unused"})
public class RibbonizerExtension {

    public static String NAME = "ribbonizer";

    private Set<String> forcedVariantsNames = new HashSet<>();

    private Set<String> iconNames = new HashSet<>();

    private List<FilterBuilder> filterBuilders = new ArrayList<>();

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

    public void builder(FilterBuilder filterBuilder) {
        this.filterBuilders.clear();
        this.filterBuilders.add(filterBuilder);
    }

    // utilities

    public GrayScaleFilter grayScaleFilter(ApplicationVariant variant, File iconFile) {
        return (GrayScaleFilter) new GrayScaleBuilder().apply(variant, iconFile);
    }

    public ColorRibbonFilter grayRibbonFilter(ApplicationVariant variant, File iconFile) {
        return (ColorRibbonFilter) new GrayRibbonBuilder().apply(variant, iconFile);
    }

    public ColorRibbonFilter yellowRibbonFilter(ApplicationVariant variant, File iconFile) {
        return (ColorRibbonFilter) new YellowRibbonBuilder().apply(variant, iconFile);
    }

    public ColorRibbonFilter greenRibbonFilter(ApplicationVariant variant, File iconFile) {
        return (ColorRibbonFilter) new GreenRibbonBuilder().apply(variant, iconFile);
    }

    public ColorRibbonFilter customColorRibbonFilter(ApplicationVariant variant, File iconFile, String nm) {
        return (ColorRibbonFilter) new CustomColorRibbonBuilder(nm).apply(variant, iconFile);
    }
}
