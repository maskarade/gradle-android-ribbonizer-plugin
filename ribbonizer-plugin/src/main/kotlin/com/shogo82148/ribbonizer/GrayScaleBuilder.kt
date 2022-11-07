package com.shogo82148.ribbonizer

import com.android.build.gradle.api.ApplicationVariant
import com.shogo82148.ribbonizer.filter.GrayScaleFilter
import com.shogo82148.ribbonizer.resource.Resource
import com.shogo82148.ribbonizer.resource.Variant
import java.io.File
import java.util.function.Consumer

class GrayScaleBuilder : FilterBuilder {
    override fun apply(variant: Variant, iconFile: File): Consumer<Resource> {
        return GrayScaleFilter()
    }
}