package com.github.gfx.ribbonizer;

import com.android.build.gradle.api.ApplicationVariant;
import com.github.gfx.ribbonizer.filter.ColorRibbonFilter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.Consumer;

public class GreenRibbonBuilder implements FilterBuilder {

    public static Color COLOR = new Color(0, 0x72, 0, 0x99);

    @Override
    public Consumer<BufferedImage> apply(ApplicationVariant variant, File iconFile) {
        return new ColorRibbonFilter(variant.getBuildType().getName(), COLOR);
    }
}
