package com.github.gfx.ribbonizer;

import com.android.build.gradle.api.ApplicationVariant;
import com.github.gfx.ribbonizer.filter.ColorRibbonFilter;
import com.github.gfx.ribbonizer.resource.Resource;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.Consumer;

public class CustomColorRibbonBuilder implements FilterBuilder {

    private String nm;

    public CustomColorRibbonBuilder(String nm) {
        this.nm = nm;
    }

    @Override
    public Consumer<Resource> apply(ApplicationVariant variant, File iconFile) {
        return new ColorRibbonFilter(variant.getBuildType().getName(), Color.decode(nm));
    }
}
