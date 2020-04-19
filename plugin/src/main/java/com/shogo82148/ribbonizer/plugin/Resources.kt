package com.shogo82148.ribbonizer.plugin

import com.android.build.gradle.internal.utils.toImmutableList
import groovy.util.XmlSlurper
import groovy.util.slurpersupport.GPathResult
import java.io.File

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
        val manifestXml = XmlSlurper().parse(manifestFile)
        val applicationNode = manifestXml.getProperty("application") as GPathResult

        val icon = applicationNode.getProperty("@android:icon").toString()
        val roundIcon = applicationNode.getProperty("@android:roundIcon").toString()

        val icons = ArrayList<String>(2)
        if (icon.isNotEmpty()) {
            icons.add(icon)
        }
        if (roundIcon.isNotEmpty()) {
            icons.add(roundIcon)
        }
        return icons.toImmutableList()
    }

    fun adaptiveIconResource(adaptiveIcon: File): String {
        val iconXml = XmlSlurper().parse(adaptiveIcon)

        val foreground = iconXml.getProperty("foreground") as GPathResult
        val foregroundDrawable = foreground.getProperty("@android:drawable").toString()
        if (foregroundDrawable.startsWith("@mipmap/") || foregroundDrawable.startsWith("@drawable/")) {
            return foregroundDrawable
        }

        val background = iconXml.getProperty("background") as GPathResult
        val backgroundDrawable = background.getProperty("@android:drawable").toString()
        if (backgroundDrawable.startsWith("@mipmap/") || backgroundDrawable.startsWith("@drawable/")) {
            return backgroundDrawable
        }

        return ""
    }
}
