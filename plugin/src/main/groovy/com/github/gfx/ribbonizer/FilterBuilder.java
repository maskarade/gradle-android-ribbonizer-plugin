package com.github.gfx.ribbonizer;

import com.android.build.gradle.api.ApplicationVariant;

import org.gradle.api.Action;

import java.awt.image.BufferedImage;
import java.util.function.Function;

public interface FilterBuilder extends Function<ApplicationVariant, Action<BufferedImage>> {

}
