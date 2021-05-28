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

    var forcedVariantsNames: Set<String> = HashSet()
    var filterBuilders: List<FilterBuilder> = ArrayList()

    companion object {
        const val NAME = "ribbonizer"
    }
}