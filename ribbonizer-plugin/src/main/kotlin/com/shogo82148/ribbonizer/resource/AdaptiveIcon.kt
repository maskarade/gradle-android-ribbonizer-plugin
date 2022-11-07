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
class AdaptiveIcon(ribbonizer: Ribbonizer, file: File) : Resource(ribbonizer, file) {
    val document: Document
    val foreground: String
    val background: String
    init {
        val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        document = documentBuilder.parse(file)

        val xpath = XPathFactory.newInstance().newXPath()
        val foregroundNode = xpath.evaluate("/adaptive-icon/foreground", document, XPathConstants.NODE) as Node?
        foreground = foregroundNode?.attributes?.getNamedItem("android:drawable")?.nodeValue ?: ""

        val backgroundNode = xpath.evaluate("/adaptive-icon/background", document, XPathConstants.NODE) as Node?
        background = backgroundNode?.attributes?.getNamedItem("android:drawable")?.nodeValue ?: ""
    }

    override fun apply(filter: Filter) {
        filter.apply(this)
    }

    override fun save(file: File) {
        val tfFactory = TransformerFactory.newInstance()
        val tf = tfFactory.newTransformer()

        tf.setOutputProperty("indent", "yes")
        tf.setOutputProperty("encoding", "UTF-8")
        tf.transform(DOMSource(document), StreamResult(file));
    }

    fun processForeground() {
//        if (foreground == "") {
//            return
//        }
//        ribbonizer.findResourceFiles(foreground).forEach {
//            when (it.extension) {
//                "xml" -> {
//                    val icon = VectorAdaptiveIcon(ribbonizer, it)
//                    ribbonizer.process(icon)
//                }
//                "png" -> {
//                    val icon = ImageAdaptiveIcon(ribbonizer, it)
//                    ribbonizer.process(icon)
//                }
//            }
//        }
    }

    fun processBackground() {
//        if (background == "") {
//            return
//        }
//        ribbonizer.findResourceFiles(background).forEach {
//            when (it.extension) {
//                "xml" -> {
//                    val icon = VectorAdaptiveIcon(ribbonizer, it)
//                    ribbonizer.process(icon)
//                }
//                "png" -> {
//                    val icon = ImageAdaptiveIcon(ribbonizer, it)
//                    ribbonizer.process(icon)
//                }
//            }
//        }
    }
}