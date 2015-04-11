package com.github.gfx.ribbonizer;

import com.android.build.gradle.api.ApplicationVariant;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public interface FilterBuilder extends BiFunction<ApplicationVariant, File, Consumer<BufferedImage>> {

}
