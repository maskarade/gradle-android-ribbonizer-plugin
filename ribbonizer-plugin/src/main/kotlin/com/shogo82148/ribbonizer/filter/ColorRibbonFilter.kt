package com.shogo82148.ribbonizer.filter

import com.shogo82148.ribbonizer.resource.*
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.font.FontRenderContext
import java.awt.geom.PathIterator
import java.awt.image.BufferedImage
import java.util.function.Consumer
import kotlin.math.pow
import kotlin.math.sqrt

class ColorRibbonFilter(
    var label: String,
    var ribbonColor: Color,
    var labelColor: Color = Color.WHITE
): Consumer<Resource>, Filter {
    var fontName = "Default"
    var fontStyle = Font.PLAIN
    var largeRibbon = false

    override fun accept(rsc: Resource) {
        rsc.apply(this)
    }

    override fun apply(icon: ImageIcon) {
        val image = icon.image
        val width = image.width
        val height = image.height
        val g = image.graphics as Graphics2D
        g.rotate(Math.toRadians(-45.0))
        val y = height / if (largeRibbon) 2 else 4

        // calculate the rectangle where the label is rendered
        val frc = FontRenderContext(g.transform, true, true)
        val maxLabelWidth = calculateMaxLabelWidth(y)
        g.font = getFont(maxLabelWidth, frc)
        val labelBounds = g.font.getStringBounds(label, frc)

        // draw the ribbon
        g.color = ribbonColor
        g.fillRect(-width, y, width * 2, labelBounds.height.toInt())
        // draw the label
        g.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        )
        g.color = labelColor
        val fm = g.fontMetrics
        drawString(
            g,
            label,
            (-labelBounds.width).toInt() / 2,
            y + fm.ascent
        )
        g.dispose()
    }

    override fun apply(icon: AdaptiveIcon) {
        if (icon.foreground != null) {
            icon.processForeground()
        } else if (icon.background != null) {
            icon.processBackground()
        }
    }

    override fun apply(icon: ImageAdaptiveIcon) {
        // https://medium.com/google-design/designing-adaptive-icons-515af294c783
        // Adaptive icons are 108dp*108dp in size but are masked to a maximum of 72dp*72dp.
        val maskSize = 72
        val imageSize = 108
        val image = icon.image
        val width = image.width * maskSize / imageSize
        val height = image.height * maskSize / imageSize
        val g = image.graphics as Graphics2D
        g.rotate(Math.toRadians(-45.0))
        val offset = (1.0 - maskSize.toDouble() / imageSize.toDouble()) / 2.0 * sqrt(2.0)
        g.translate(0.0, image.height * offset)
        val y = height / if (largeRibbon) 2 else 4

        // calculate the rectangle where the label is rendered
        val frc = FontRenderContext(g.transform, true, true)
        val maxLabelWidth = calculateMaxLabelWidth(y)
        g.font = getFont(maxLabelWidth, frc)
        val labelBounds = g.font.getStringBounds(label, frc)

        // draw the ribbon
        g.color = ribbonColor
        g.fillRect(-width, y, width * 2, labelBounds.height.toInt())
        // draw the label
        g.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        )
        g.color = labelColor
        val fm = g.fontMetrics
        drawString(
            g, label,
            (-labelBounds.width).toInt() / 2,
            y + fm.ascent
        )
        g.dispose()
    }

    override fun apply(icon: VectorAdaptiveIcon) {
        val document = icon.document
        val root = document.firstChild

        // https://medium.com/google-design/designing-adaptive-icons-515af294c783
        // Adaptive icons are 108dp*108dp in size but are masked to a maximum of 72dp*72dp.
        val maskSize = 72.0
        val imageSize = 108.0
        val viewportWidth = root.attributes.getNamedItem("android:viewportWidth").nodeValue.toString().toDouble()
        val viewportHeight = root.attributes.getNamedItem("android:viewportHeight").nodeValue.toString().toDouble()
        val width = viewportWidth * maskSize / imageSize
        val height = viewportHeight * maskSize / imageSize
        val offset = (1.0 - maskSize / imageSize) / 2.0 * viewportHeight
        val y = height / if (largeRibbon) 2 else 4

        // rotate the ribbon
        val group = document.createElement("group")
        group.setAttribute("android:rotation", "-45")
        group.setAttribute("android:translateX", "%.1f".format(offset))
        group.setAttribute("android:translateY", "%.1f".format(offset))

        // calculate font size
        val g = BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics()
        val frc = g.fontRenderContext
        val maxLabelWidth = calculateMaxLabelWidth(y.toInt())
        val font = getFont(maxLabelWidth, frc)
        val labelBounds = font.getStringBounds(label, frc)
        g.font = font

        // create the ribbon
        val ribbon = document.createElement("path")
        ribbon.setAttribute(
            "android:fillColor",
            "#%02x%02x%02x".format(ribbonColor.red, ribbonColor.green, ribbonColor.blue)
        )
        ribbon.setAttribute("android:fillAlpha", (ribbonColor.alpha/255.0).toString())
        ribbon.setAttribute(
            "android:pathData",
            "M${-width} $y H${width*2} v${labelBounds.height} H${-width} Z"
        )
        group.appendChild(ribbon)

        // get the outline of the label text
        // ref. https://wcs.hatenablog.com/entry/2014/08/02/184622
        val labelGroup = document.createElement("group")
        labelGroup.setAttribute("android:translateX", "%.1f".format(-labelBounds.width / 2.0))
        labelGroup.setAttribute("android:translateY", "%.1f".format(y+g.fontMetrics.ascent.toDouble()))
        val iter = font.createGlyphVector(frc, label).outline.getPathIterator(null)
        val coords = FloatArray(6)
        var pathData = ""
        while (!iter.isDone) {
            when (iter.currentSegment(coords)) {
                PathIterator.SEG_MOVETO -> {
                    pathData += "M%.2f %.2f ".format(coords[0], coords[1])
                }
                PathIterator.SEG_LINETO -> {
                    pathData += "L%.2f %.2f ".format(coords[0], coords[1])
                }
                PathIterator.SEG_CLOSE -> {
                    pathData += "Z "
                }
                PathIterator.SEG_QUADTO -> {
                    pathData += "Q %.2f %.2f, %.2f %.2f ".format(coords[0], coords[1], coords[2], coords[3])
                }
                PathIterator.SEG_CUBICTO -> {
                    pathData += "C %.2f %.2f, %.2f %.2f, %.2f %.2f ".format(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5])
                }
            }
            iter.next()
        }
        val path = document.createElement("path")
        path.setAttribute(
            "android:fillColor",
            "#%02x%02x%02x".format(labelColor.red, labelColor.green, labelColor.blue)
        )
        path.setAttribute("android:fillAlpha", (labelColor.alpha.toDouble()/255.0).toString())
        path.setAttribute("android:pathData", pathData)
        labelGroup.appendChild(path)

        group.appendChild(labelGroup)
        root.appendChild(group)
    }

    private fun getFont(maxLabelWidth: Int, frc: FontRenderContext): Font {
        var max = 32
        if (label == "") {
            return Font(fontName, fontStyle, max / 2)
        }
        var min = 0
        var x = max
        for (i in 0..9) {
            val m = (max + min) / 2
            if (m == x) {
                break
            }
            val font = Font(fontName, fontStyle, m)
            val labelBounds = font.getStringBounds(label, frc)
            val px = labelBounds.width.toInt()
            if (px > maxLabelWidth) {
                max = m
            } else {
                min = m
            }
            x = m
        }
        return Font(fontName, fontStyle, x)
    }

    companion object {
        private val debug = System.getenv("RIBBONIZER_DEBUG")?.toBoolean() ?: false

        private fun calculateMaxLabelWidth(y: Int): Int {
            return sqrt(y.toDouble().pow(2.0) * 2).toInt()
        }

        private fun drawString(g: Graphics2D, str: String, x: Int, y: Int) {
            g.drawString(str, x, y)
            if (debug) {
                val fm = g.fontMetrics
                val bounds = g.font.getStringBounds(
                    str,
                    FontRenderContext(g.transform, true, true)
                )
                g.drawRect(x, y - fm.ascent, bounds.width.toInt(), fm.ascent)
            }
        }
    }
}