package com.github.gfx.ribbonizer
import groovy.transform.CompileStatic

@CompileStatic
class RibbonizerExtension {
    public static String NAME = "ribbonizer"

    // TODO: get names from AndroidManifest.xml
    Set<String> iconNames = new HashSet<>(["ic_launcher"])

    List<? extends FilterBuilder> filterBuilders = [
        new GreenRibbonBuilder() as FilterBuilder
    ]

    public void iconNames(List<String> iconNames) {
        this.iconNames = new HashSet<>(iconNames)
    }

    public void filterBuilders(Collection<? extends FilterBuilder> filterBuilders) {
        this.filterBuilders = new ArrayList<>(filterBuilders)
    }

    public void builder(Class<? extends FilterBuilder> builderClass) {
        this.filterBuilders = [ builderClass.newInstance() as FilterBuilder ]
    }
}