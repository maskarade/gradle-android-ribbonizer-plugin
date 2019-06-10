package com.shogo82148.ribbonizer;

import com.android.build.gradle.api.ApplicationVariant;
import com.shogo82148.ribbonizer.filter.ColorRibbonFilter;
import com.shogo82148.ribbonizer.resource.Resource;

import java.awt.Color;
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
