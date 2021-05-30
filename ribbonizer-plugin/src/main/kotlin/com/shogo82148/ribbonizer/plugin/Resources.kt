package com.shogo82148.ribbonizer.plugin

import com.android.build.gradle.internal.utils.toImmutableList
import org.w3c.dom.Node
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

object Resources {
    fun resourceFilePattern(name: String): String {
        return if (name.startsWith("@")) {
            val pair = name.substring(1).split("/", limit = 2)
            val baseResType = pair[0]
            val filename = pair[1]
            "$baseResType*/$filename.*"
        } else {
            name
        }
    }

    fun launcherIcons(manifestFile: File): List<String> {
        val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val document = documentBuilder.parse(manifestFile)

        val xpath = XPathFactory.newInstance().newXPath()
        val applicationNode = xpath.evaluate("/manifest/application", document, XPathConstants.NODE) as Node

        val icons = ArrayList<String>(2)
        applicationNode.attributes.getNamedItem("android:icon")?.nodeValue?.let {
            icons.add(it)
        }
        applicationNode.attributes.getNamedItem("android:roundIcon")?.nodeValue?.let {
            icons.add(it)
        }
        return icons.toImmutableList()
    }
}