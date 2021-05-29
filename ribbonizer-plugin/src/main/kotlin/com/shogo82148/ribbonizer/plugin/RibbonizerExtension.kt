package com.shogo82148.ribbonizer.plugin

import com.android.build.gradle.api.ApplicationVariant
import com.shogo82148.ribbonizer.*
import com.shogo82148.ribbonizer.filter.ColorRibbonFilter
import com.shogo82148.ribbonizer.filter.GrayScaleFilter
import com.shogo82148.ribbonizer.resource.Resource
import java.io.File
import java.util.function.BiFunction
import java.util.function.Consumer

open class RibbonizerExtension {
    private var _iconNames: MutableSet<String> = HashSet()

    val iconNames: Set<String>
        get() = _iconNames

    fun setIconNames(resNames: Collection<String>) {
        _iconNames = HashSet(resNames)
    }

    fun iconNames(resNames: Collection<String>) {
        _iconNames = HashSet(resNames)
    }

    fun iconNames(vararg resNames: String) {
        _iconNames = HashSet(listOf(*resNames))
    }

    fun iconName(resName: String) {
        _iconNames.add(resName)
    }

    private var _forcedVariantsNames: MutableSet<String> = HashSet()

    val forcedVariantsNames: Set<String>
        get() = _forcedVariantsNames

    fun setForcedVariantsNames(variantNames: Collection<String>) {
        _forcedVariantsNames = HashSet(variantNames)
    }

    fun forcedVariantsNames(variantNames: Collection<String>) {
        _forcedVariantsNames = HashSet(variantNames)
    }

    fun forcedVariantsNames(vararg variantNames: String) {
        _forcedVariantsNames = HashSet(listOf(*variantNames))
    }

    private var _filterBuilders: MutableList<FilterBuilder> = ArrayList()

    val filterBuilders: List<FilterBuilder>
        get() = _filterBuilders

    fun setFilterBuilders(filterBuilders: Collection<FilterBuilder>) {
        _filterBuilders = ArrayList(filterBuilders)
    }

    fun builder(filterBuilder: (ApplicationVariant, File) -> Consumer<Resource>?) {
        val fb = object: FilterBuilder{
            override fun apply(variant: ApplicationVariant, file: File): Consumer<Resource>? {
                return filterBuilder(variant, file)
            }
        }
        _filterBuilders.clear()
        _filterBuilders.add(fb)
    }

    // utilities

    fun grayScaleFilter(variant: ApplicationVariant, iconFile: File): GrayScaleFilter {
        return GrayScaleBuilder().apply(variant, iconFile) as GrayScaleFilter
    }

    fun grayRibbonFilter(variant: ApplicationVariant, iconFile: File): ColorRibbonFilter {
        return GrayRibbonBuilder().apply(variant, iconFile) as ColorRibbonFilter
    }

    fun yellowRibbonFilter(variant: ApplicationVariant, iconFile: File): ColorRibbonFilter {
        return YellowRibbonBuilder().apply(variant, iconFile) as ColorRibbonFilter
    }

    fun greenRibbonFilter(variant: ApplicationVariant, iconFile: File): ColorRibbonFilter {
        return GreenRibbonBuilder().apply(variant, iconFile) as ColorRibbonFilter
    }

    fun customColorRibbonFilter(
        variant: ApplicationVariant,
        iconFile: File,
        nm: String
    ): ColorRibbonFilter {
        return CustomColorRibbonBuilder(nm).apply(variant, iconFile) as ColorRibbonFilter
    }

    companion object {
        const val NAME = "ribbonizer"
    }
}