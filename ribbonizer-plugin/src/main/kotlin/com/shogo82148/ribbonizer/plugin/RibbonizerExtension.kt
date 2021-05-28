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

    private var _filterBuilders: MutableList<FilterBuilder> = ArrayList()

    val filterBuilders: List<FilterBuilder>
        get() = _filterBuilders

    fun setFilterBuilders(filterBuilders: Collection<FilterBuilder>) {
        _filterBuilders = ArrayList(filterBuilders)
    }

    fun builder(filterBuilder: FilterBuilder) {
        _filterBuilders.clear()
        _filterBuilders.add(filterBuilder)
    }

    companion object {
        const val NAME = "ribbonizer"
    }
}