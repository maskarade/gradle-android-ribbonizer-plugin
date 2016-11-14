package com.github.gfx.ribbonizer;

import com.android.build.gradle.api.ApplicationVariant;
import com.github.gfx.ribbonizer.filter.ColorRibbonFilter;
import com.github.gfx.ribbonizer.filter.GrayScaleFilter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.Consumer;

public class GrayScaleBuilder implements FilterBuilder {

    @Override
    public Consumer<BufferedImage> apply(ApplicationVariant applicationVariant, File iconFile) {
        return new GrayScaleFilter();
    }

    @Override
    public Consumer<BufferedImage> apply(String label, File iconFile) {
        return new GrayScaleFilter();
    }
}
