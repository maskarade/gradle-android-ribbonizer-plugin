package com.github.gfx.ribbonizer;

import com.android.build.gradle.api.ApplicationVariant;
import com.github.gfx.ribbonizer.filter.GrayScaleFilter;
import com.github.gfx.ribbonizer.resource.Resource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.Consumer;

public class GrayScaleBuilder implements FilterBuilder {

    @Override
    public Consumer<Resource> apply(ApplicationVariant applicationVariant, File iconFile) {
        return new GrayScaleFilter();
    }
}
