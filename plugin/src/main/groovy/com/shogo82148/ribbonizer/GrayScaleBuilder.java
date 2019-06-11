package com.shogo82148.ribbonizer;

import com.android.build.gradle.api.ApplicationVariant;
import com.shogo82148.ribbonizer.filter.GrayScaleFilter;
import com.shogo82148.ribbonizer.resource.Resource;

import java.io.File;
import java.util.function.Consumer;

public class GrayScaleBuilder implements FilterBuilder {

    @Override
    public Consumer<Resource> apply(ApplicationVariant applicationVariant, File iconFile) {
        return new GrayScaleFilter();
    }
}
