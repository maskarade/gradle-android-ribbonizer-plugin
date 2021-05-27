package com.shogo82148.ribbonizer.filter

import com.shogo82148.ribbonizer.resource.Filter
import com.shogo82148.ribbonizer.resource.Resource
import com.shogo82148.ribbonizer.resource.ImageIcon
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.font.FontRenderContext
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
        val labelBounds =
            g.font.getStringBounds(label, frc)

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