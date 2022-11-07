package com.shogo82148.ribbonizer.plugin

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

    fun findResourceFiles(baseDir: File, names: List<String>): List<File> {
        val files = ArrayList<File>()
        names.forEach {
            files.addAll(findResourceFiles(baseDir, it))
        }
        return files
    }

    fun findResourceFiles(baseDir: File, name: String): List<File> {
        val files = ArrayList<File>()
        if (name.startsWith("@")) {
            val pair = name.substring(1).split("/", limit = 2)
            val baseResType = pair[0]
            val filename = pair[1]
            findResType(baseDir, baseResType).forEach { dir ->
                dir.listFiles()?.forEach { file ->
                    if (file.nameWithoutExtension == filename) {
                        files.add(file)
                    }
                }
            }
        }
        return files
    }

    private fun findResType(baseDir: File, baseResType: String): List<File> {
        val dirs = ArrayList<File>()
        if (!baseDir.exists()) {
            return dirs
        }
        baseDir.listFiles()?.forEach {
            if (!it.isDirectory) {
                return@forEach
            }
            if (matchResType(it, baseResType)) {
                dirs.add(it)
            } else {
                dirs.addAll(findResType(it, baseResType))
            }
        }
        return dirs
    }

    private fun matchResType(file: File, baseResType: String): Boolean {
        if (!file.isDirectory) {
            return false
        }
        val pair = file.name.split("-", limit=2)
        return pair[0] == baseResType
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
        return icons
    }
}