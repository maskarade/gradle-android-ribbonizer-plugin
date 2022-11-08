package com.shogo82148.ribbonizer.plugin

import com.shogo82148.ribbonizer.FilterBuilder
import com.shogo82148.ribbonizer.resource.AdaptiveIcon
import com.shogo82148.ribbonizer.resource.ImageIcon
import com.shogo82148.ribbonizer.resource.Resource
import com.shogo82148.ribbonizer.resource.Variant
import org.gradle.api.Project
import org.w3c.dom.Node
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

class Ribbonizer (
    private val name: String,
    private val project: Project,
    private val outputDir: File,
    private val iconFiles: List<File>,
    private val variant: Variant,
    private val filterBuilders: List<FilterBuilder>
) {
    fun process() {
        iconFiles.forEach {
            when (it.extension) {
                "xml" -> {
                    processAdaptiveIcon(it)
                }
                "png" -> {
                    val icon = ImageIcon(this, it)
                    process(icon)
                }
            }
        }
    }

    fun process(resource: Resource) {
        try {
            val file = resource.file
            info("process $file")

            val basename = file.name
            val resType = file.parentFile.name
            val outputFile = File(outputDir, "$resType/$basename")
            outputFile.parentFile.mkdirs()
            filterBuilders.forEach { filterBuilder: FilterBuilder ->
                val filter = filterBuilder.apply(variant, file)
                filter?.accept(resource)
            }
            info("saving to $outputFile")
            resource.save(outputFile)
        } catch (e: Exception) {
            info("Exception: $e")
        }
    }

    private fun processAdaptiveIcon(file: File) {
        val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val document = documentBuilder.parse(file)

        val node = document.documentElement
        if (node.tagName != "adaptive-icon") {
            return
        }

        val xpath = XPathFactory.newInstance().newXPath()
        val foregroundNode = xpath.evaluate("/adaptive-icon/foreground", document, XPathConstants.NODE) as Node?
        val foreground = foregroundNode?.attributes?.getNamedItem("android:drawable")?.nodeValue
        val foregroundFile = foreground?.let {
            findDrawable(it)
        }

        val backgroundNode = xpath.evaluate("/adaptive-icon/background", document, XPathConstants.NODE) as Node?
        val background = backgroundNode?.attributes?.getNamedItem("android:drawable")?.nodeValue
        val backgroundFile = background?.let {
            findDrawable(it)
        }

        val resource = AdaptiveIcon(this, file, foregroundFile, backgroundFile)
        process(resource)
    }

    private fun findDrawable(name: String): File? {
        if (!name.startsWith("@")) {
            return null
        }
        val pair = name.substring(1).split("/", limit = 2)
        val baseResType = pair[0]
        val filename = pair[1]
        iconFiles.forEach {
            val resTypePair = it.parentFile.name.split("-", limit=2)
            val resType = resTypePair[0]
            if (resType == baseResType && it.nameWithoutExtension == filename) {
                return it
            }
        }
        return null
    }

    private fun info(message: String) {
        project.logger.info("[$name] \uD83C\uDF80 $message")
    }
}