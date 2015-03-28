package com.github.gfx.ribbonizer

import groovy.transform.CompileStatic

@CompileStatic
class RibbonizerExtension {
    public static String NAME = "ribbonize"

    // TODO: get names from AndroidManifest.xml
    Set<String> iconNames = new HashSet<>(["ic_launcher"])

    public void setIconNames(List<String> iconNames) {
        this.iconNames = new HashSet<>(iconNames)
    }
}