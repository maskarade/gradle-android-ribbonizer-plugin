package com.shogo82148.ribbonizer.filter

import com.shogo82148.ribbonizer.resource.*
import org.w3c.dom.Document
import org.w3c.dom.Node
import java.util.function.Consumer
import kotlin.math.roundToInt

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

    override fun apply(icon: AdaptiveIcon) {
        icon.processForeground()
        icon.processBackground()
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

    override fun apply(icon: VectorAdaptiveIcon) {
        apply(icon.document, icon.document.firstChild)
    }

    private fun apply(document: Document, node: Node) {
        if (node.nodeType != Node.ELEMENT_NODE) {
            return
        }
        when (node.nodeName) {
            "path" -> {
                node.attributes.getNamedItem("android:fillColor")?.let {
                    it.nodeValue = toGray(it.nodeValue)
                }
                node.attributes.getNamedItem("android:strokeColor")?.let {
                    it.nodeValue = toGray(it.nodeValue)
                }
                val children = node.childNodes
                for (i in 0 until children.length) {
                    apply(document, children.item(i))
                }
            }
            "gradient" -> {
                val children = node.childNodes
                for (i in 0 until children.length) {
                    val child = children.item(i)
                    if (child.nodeType != Node.ELEMENT_NODE || child.nodeName != "item") {
                        continue
                    }
                    child.attributes.getNamedItem("android:color")?.let {
                        it.nodeValue = toGray(it.nodeValue)
                    }
                }
            }
            else -> {
                val children = node.childNodes
                for (i in 0 until children.length) {
                    apply(document, children.item(i))
                }
            }
        }
    }
}

private fun toGray(color: Int): Int {
    val a = (color and 0xFF000000.toInt())
    val r = ((color and 0x00FF0000) shr 16)
    val g = ((color and 0x0000FF00) shr 8)
    val b = (color and 0x000000FF)
    val c = ((2 * r + 4 * g + b).toDouble() / 7.0).roundToInt()
    return a or (c shl 16) or (c shl 8) or c
}

private fun toGray(color: String): String {
    return if (color.startsWith("#")) {
        val hex = color.substring(1)
        val v = hex.toInt(16)
        when (hex.length) {
            8 -> {
                val a = (v and 0xFF000000.toInt()) shr 24
                val r = (v and 0x00FF0000) shr 16
                val g = (v and 0x0000FF00) shr 8
                val b = (v and 0x000000FF)
                val c = ((2 * r + 4 * g + b).toDouble() / 7.0).roundToInt()
                "#%02X%02X%02X%02X".format(a, c, c, c)
            }
            6 -> {
                val r = (v and 0xFF0000) shr 16
                val g = (v and 0x00FF00) shr 8
                val b = (v and 0x0000FF)
                val c = ((2 * r + 4 * g + b).toDouble() / 7.0).roundToInt()
                "#%02X%02X%02X".format(c, c, c)
            }
            4 -> {
                var a = (v and 0xF000) shr 12
                a = (a shl 4) or a
                var r = (v and 0xF00) shr 8
                r = (r shl 4) or r
                var g = (v and 0x0F0) shr 4
                g = (g shl 4) or g
                var b = (v and 0x00F)
                b = (b shl 4) or b
                ((2 * r + 4 * g + b).toDouble() / 7.0).roundToInt()
                val c = ((2 * r + 4 * g + b).toDouble() / 7.0).roundToInt()
                "#%02X%02X%02X%02X".format(a, c, c, c)
            }
            3 -> {
                var r = ((v and 0xF00) shr 8)
                r = (r shl 4) or r
                var g = ((v and 0x0F0) shr 4)
                g = (g shl 4) or g
                var b = (v and 0x00F)
                b = (b shl 4) or b
                ((2 * r + 4 * g + b).toDouble() / 7.0).roundToInt()
                val c = ((2 * r + 4 * g + b).toDouble() / 7.0).roundToInt()
                "#%02X%02X%02X".format(c, c, c)
            }
            else -> {
                return color
            }
        }
    } else {
        color
    }
}