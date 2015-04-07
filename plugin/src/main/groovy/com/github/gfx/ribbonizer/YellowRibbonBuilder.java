package com.github.gfx.ribbonizer;

import com.android.build.gradle.api.ApplicationVariant;

import org.gradle.api.Action;

import java.awt.*;
import java.awt.image.BufferedImage;

public class YellowRibbonBuilder implements FilterBuilder {

    @Override
    public Action<BufferedImage> apply(ApplicationVariant variant) {
        return new ColorRibbonFilter(variant, new Color(0xff, 0x76, 0, 0xaa));
    }
}
