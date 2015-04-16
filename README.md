# Ribbonizer plugin for Android [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.gfx.ribbonizer/plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.gfx.ribbonizer/plugin) [![Circle CI](https://circleci.com/gh/gfx/gradle-android-ribbonizer-plugin.svg?style=svg&branch=master)](https://circleci.com/gh/gfx/gradle-android-ribbonizer-plugin)

This is a ribbonizer as a Gradle plugin for Android, which adds a ribbon to launcher icons.

![](ic-beta.png) ![](ic-debug.png)

## Usage

```groovy
// in build.gradle
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:1.1.3'
        classpath 'com.github.gfx.ribbonizer:plugin:0.3.0'
    }
}
```

```groovy
// in app/build.gradle
apply plugin: 'com.github.gfx.ribbonizer'

android {
    // ...

    buildTypes {
        debug {}
        beta {
            debuggable true
        }
        release {}
    }

    productFlavors {
        local {}
        staging {}
        production {}
    }
}

ribbonizer {
    // "manifest application[android:icon]" is automatically added to the list
    iconNames "@drawable/ic_notification", "@drawable/widget_preview"

    builder { ApplicationVariant variant, File iconFile ->
        // change ribbon colors by product flavors
        if (variant.flavorName == "local") {
            return grayRibbonFilter(variant, iconFile)
        } else if (variant.flavorName == "staging") {
            return yellowRibbonFilter(variant, iconFile)
        } else {
            return greenRibbonFilter(variant, iconFile)
        }
    }
}

```


## Project Structure

```
plugin/   - The main module of a Gradle plugin
example/  - An example android application that uses this plugin
buildSrc/ - A helper module to use this plugin in example modules
```

## Author And License

Copyright 2015, FUJI Goro (gfx) <gfuji@cpan.org>. All rights reserved.

This library may be copied only under the terms of the Apache License 2.0, which may be found in the distribution.
