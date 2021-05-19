package com.shogo82148.ribbonizer.plugin

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class ResourcesTest {
    @Test fun `get launcher icons`() {
        val projectDir = File("build/ResourcesTest")
        projectDir.mkdirs()
        val manifest = projectDir.resolve("AndroidManifest.xml")
        manifest.writeText("""<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.shogo82148.ribbonizer.example">
    <application
        android:icon="@mipmap/ic_launcher">
    </application>
</manifest>""")
        val icons = Resources.launcherIcons(manifest)
        assertEquals(listOf("@mipmap/ic_launcher"), icons)
    }

    @Test fun `get launcher round icons`() {
        val projectDir = File("build/ResourcesTest")
        projectDir.mkdirs()
        val manifest = projectDir.resolve("AndroidManifest.xml")
        manifest.writeText("""<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.shogo82148.ribbonizer.example">
    <application
        android:icon="@mipmap/ic_launcher"
        android:roundIcon="@mipmap/ic_launcher_round">
    </application>
</manifest>""")
        val icons = Resources.launcherIcons(manifest)
        assertEquals(listOf("@mipmap/ic_launcher", "@mipmap/ic_launcher_round"), icons)
    }
}