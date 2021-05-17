package com.shogo82148.ribbonizer.resource

interface Filter {
    fun apply(icon: ImageAdaptiveIcon)
    fun apply(icon: ImageIcon)
}
