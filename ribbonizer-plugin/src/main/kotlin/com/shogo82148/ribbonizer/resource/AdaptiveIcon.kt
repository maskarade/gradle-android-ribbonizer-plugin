package com.shogo82148.ribbonizer.resource

import com.shogo82148.ribbonizer.plugin.Ribbonizer
import org.w3c.dom.Document
import org.w3c.dom.Node
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

// AdaptiveIcon is an adaptive icon
class AdaptiveIcon(
    ribbonizer: Ribbonizer, file: File, foreground: File?, background: File?
) : Resource(ribbonizer, file) {
    val foreground: File?
    val background: File?
    init {
        this.foreground = foreground
        this.background = background
    }
    override fun apply(filter: Filter) {
        filter.apply(this)
    }

    override fun save(file: File) {
    }

    fun processForeground() {
        foreground?.let {
            when (it.extension) {
                "xml" -> {
                    val icon = VectorAdaptiveIcon(ribbonizer, it)
                    ribbonizer.process(icon)
                }
                "png" -> {
                    val icon = ImageAdaptiveIcon(ribbonizer, it)
                    ribbonizer.process(icon)
                }
            }
        }
    }

    fun processBackground() {
        background?.let {
            when (it.extension) {
                "xml" -> {
                    val icon = VectorAdaptiveIcon(ribbonizer, it)
                    ribbonizer.process(icon)
                }
                "png" -> {
                    val icon = ImageAdaptiveIcon(ribbonizer, it)
                    ribbonizer.process(icon)
                }
            }
        }
    }
}