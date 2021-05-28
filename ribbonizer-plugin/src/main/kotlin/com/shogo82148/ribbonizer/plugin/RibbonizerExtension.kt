package com.shogo82148.ribbonizer.plugin

import com.shogo82148.ribbonizer.FilterBuilder

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

    var filterBuilders: List<FilterBuilder> = ArrayList()

    companion object {
        const val NAME = "ribbonizer"
    }
}