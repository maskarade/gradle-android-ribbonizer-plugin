package com.github.gfx.ribbonizer
import groovy.transform.CompileStatic

@CompileStatic
class RibbonizerExtension {
    public static String NAME = "ribbonizer"

    // TODO: get names from AndroidManifest.xml
    Set<String> iconNames = new HashSet<>(["ic_launcher"])

    List<FilterBuilder> filterBuilders = [
        new GreenRibbonFilterBuilder() as FilterBuilder
    ]

    public void iconNames(List<String> iconNames) {
        this.iconNames = new HashSet<>(iconNames)
    }

    public void filterBuilders(List<FilterBuilder> filterBuilders) {
        this.filterBuilders = filterBuilders
    }
}