package com.shogo82148.ribbonizer.filter

import com.shogo82148.ribbonizer.resource.Filter
import com.shogo82148.ribbonizer.resource.ImageAdaptiveIcon
import com.shogo82148.ribbonizer.resource.ImageIcon
import com.shogo82148.ribbonizer.resource.Resource
import java.util.function.Consumer

class GrayScaleFilter : Consumer<Resource>, Filter {
    override fun accept(rsc: Resource) {
        rsc.apply(this)
    }

    override fun apply(icon: ImageIcon) {
        val image = icon.image
        val width = image.width
        val height = image.height
        for (y in 0 until height) {
            for (x in 0 until width) {
                val color = image.getRGB(x, y)
                image.setRGB(x, y, toGray(color))
            }
        }
    }

    override fun apply(icon: ImageAdaptiveIcon) {
        val image = icon.image
        val width = image.width
        val height = image.height
        for (y in 0 until height) {
            for (x in 0 until width) {
                val color = image.getRGB(x, y)
                image.setRGB(x, y, toGray(color))
            }
        }
    }

}

private fun toGray(color: Int): Int {
    val a = (color and 0xFF000000.toInt())
    val r = (color and 0x00FF0000) shr 16
    val g = (color and 0x0000FF00) shr 8
    val b = (color and 0x000000FF)
    val c = ((2.0 * r + 4.0 * g + b) / 7.0).toInt()
    return a or (c shl 16) or (c shl 8) or c
}
