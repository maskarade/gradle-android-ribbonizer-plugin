package com.github.gfx.ribbonizer;

import com.android.build.gradle.api.ApplicationVariant;

import java.awt.image.BufferedImage;
import java.util.function.Consumer;
import java.util.function.Function;

public interface FilterBuilder extends Function<ApplicationVariant, Consumer<BufferedImage>> {

}
