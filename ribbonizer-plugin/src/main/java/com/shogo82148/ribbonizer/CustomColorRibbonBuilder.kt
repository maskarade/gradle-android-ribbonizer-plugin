package com.shogo82148.ribbonizer

import com.android.build.gradle.api.ApplicationVariant
import com.shogo82148.ribbonizer.filter.ColorRibbonFilter
import com.shogo82148.ribbonizer.resource.Resource
import java.awt.Color
import java.io.File
import java.util.function.Consumer

class CustomColorRibbonBuilder(private val nm: String) : FilterBuilder {
    override fun apply(variant: ApplicationVariant, iconFile: File): Consumer<Resource> {
        return ColorRibbonFilter(variant.buildType.name, Color.decode(nm))
    }
}
