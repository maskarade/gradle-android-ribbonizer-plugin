package com.github.gfx.ribbonizer;

import com.android.build.gradle.api.ApplicationVariant;
import com.github.gfx.ribbonizer.filter.GrayScaleFilter;

import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class GrayScaleBuilder implements FilterBuilder {

    @Override
    public Consumer<BufferedImage> apply(ApplicationVariant applicationVariant) {
        return new GrayScaleFilter();
    }
}
