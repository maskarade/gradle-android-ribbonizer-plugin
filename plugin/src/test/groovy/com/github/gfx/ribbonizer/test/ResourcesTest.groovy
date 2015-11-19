package com.github.gfx.ribbonizer.test

import com.github.gfx.ribbonizer.plugin.Resources
import spock.lang.Specification

public class ResourcesTest extends Specification {
    def "resourceFilePattern"() {
        expect:
        Resources.resourceFilePattern(resName) == pattern

        where:
        resName | pattern
        "@drawable/ic_launcher" | "drawable*/ic_launcher.*"
        "@mipmap/icon" | "mipmap*/icon.*"
    }


    def "getgetLauncherIcon"() {
        setup:
        def file = File.createTempFile("AndroidManifest", ".xml")
        file.deleteOnExit()
        file.write('''
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.github.gfx.ribbonizer.example"
    android:versionCode="1"
    android:versionName="1.0" >
    <uses-sdk
        android:minSdkVersion="15"
        android:targetSdkVersion="23" />
    <application
        android:allowBackup="true"
        android:icon="@drawable/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/AppTheme" >
    </application>
</manifest>
'''.trim())

        expect:
        Resources.getLauncherIcon(file) == "@drawable/ic_launcher"
    }
}
