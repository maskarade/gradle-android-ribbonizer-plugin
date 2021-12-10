# DEPRECATED
No longer actively maintained, I recommend [Mikel's easylauncher-gradle-plugin](https://github.com/akaita/easylauncher-gradle-plugin) which might not have seen much love lately either, but seems to still work since the JFrog shutdown.

# Ribbonizer plugin for Android

[![Circle CI](https://circleci.com/gh/gfx/gradle-android-ribbonizer-plugin.svg?style=svg&branch=master)](https://circleci.com/gh/gfx/gradle-android-ribbonizer-plugin) [![Download](https://api.bintray.com/packages/gfx/maven/ribbonizer-plugin/images/download.svg)](https://bintray.com/gfx/maven/ribbonizer-plugin/_latestVersion)

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
        classpath 'com.android.tools.build:gradle:2.1.2'
        classpath 'com.github.gfx.ribbonizer:ribbonizer-plugin:2.1.0'
    }
}
```

```groovy
// in app/build.gradle
apply plugin: 'com.github.gfx.ribbonizer'

android {
    // ...

    buildTypes {
        debug {/*debuggable build, which will ribbonized automatically*/}
        beta {
            //debuggable build which will automatically ribbonized.
            debuggable true
        }
        canary {
            //non-debuggable build which will no automatically ribbonized.
            //But, we force one of its flavors. See `ribbonizer` for how-to
            debuggable false
        }
        release {/*non-debuggable build. Will not be rebbonized automatically*/}
    }

    productFlavors {
        local {}
        qa {}
        staging {}
        production {}
    }
}

ribbonizer {
    // "manifest application[android:icon]" is automatically added to the list
    iconNames "@drawable/ic_notification", "@drawable/widget_preview"

    builder { variant, iconFile ->
        // change ribbon colors by product flavors
        if (variant.flavorName == "local") {
            return grayRibbonFilter(variant, iconFile)
        } else if (variant.flavorName == "qa") {
            // customColorRibbonFilter allows setting any color code
            def filter = customColorRibbonFilter(variant, iconFile, "#00C89C")
            // Finer control of the label text can be achieved by setting it manually, or set to
            // null for an unlabelled ribbon. The default is to use the flavor name.
            filter.label = "QA" + variant.versionCode
            return filter
        } else if (variant.flavorName == "staging") {
            return yellowRibbonFilter(variant, iconFile)
        } else if (variant.buildType.name == "debug") {
            if (variant.flavorName == "production") {
                // Particular configurations can be skipped by returning no filters
                return null
            }
            else {
                // Other filters can be applied, as long as they implement Consumer<BufferedImage>
                return grayScaleFilter(variant, iconFile)
            }
        } else {
            return greenRibbonFilter(variant, iconFile)
        }
    }

    //Although `canary` build-type is marked as `non-debuggable`
    //we can still force specific variants to be ribbonized:
    forcedVariantsNames "localCanary"
}

```


## Project Structure

```
plugin/   - The main module of a Gradle plugin
example/  - An example android application that uses this plugin
buildSrc/ - A helper module to use this plugin in example modules
```

You can test this project with `./gradlew check`.

## Release Engineering

```console
./gradlew bumpMinor # or bumpMajor, bumpPatch

make publish # upload artifacts to bintray jcenter
```

## Author And License

The MIT License (MIT)

Copyright (c) 2015 FUJI Goro (gfx) <gfuji@cpan.org>.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
