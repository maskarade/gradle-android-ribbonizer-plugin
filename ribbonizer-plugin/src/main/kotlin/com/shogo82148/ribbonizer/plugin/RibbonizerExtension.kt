package com.shogo82148.ribbonizer.plugin

import com.shogo82148.ribbonizer.FilterBuilder

open class RibbonizerExtension {
    var iconNames: Set<String> = HashSet()
    var forcedVariantsNames: Set<String> = HashSet()
    var filterBuilders: List<FilterBuilder> = ArrayList()

    companion object {
        const val NAME = "ribbonizer"
    }
}