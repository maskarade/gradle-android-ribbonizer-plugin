package com.github.gfx.ribbonizer;

import com.android.build.gradle.api.ApplicationVariant;
import com.github.gfx.ribbonizer.filter.GrayScaleFilter;

import org.gradle.api.Action;

import java.awt.image.BufferedImage;

public class GrayScaleBuilder implements FilterBuilder {

    @Override
    public Action<BufferedImage> apply(ApplicationVariant applicationVariant) {
        return new GrayScaleFilter();
    }
}
