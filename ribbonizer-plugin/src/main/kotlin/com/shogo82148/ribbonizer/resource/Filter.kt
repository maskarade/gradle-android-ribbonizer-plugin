package com.shogo82148.ribbonizer.resource

interface Filter {
    fun apply(icon: AdaptiveIcon)
    fun apply(icon: ImageIcon)
    fun apply(icon: ImageAdaptiveIcon)
    fun apply(icon: VectorAdaptiveIcon)
}