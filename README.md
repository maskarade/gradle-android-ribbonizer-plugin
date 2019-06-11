# Ribbonizer plugin for Android

[![CircleCI](https://circleci.com/gh/shogo82148/gradle-android-ribbonizer-plugin.svg?style=svg)](https://circleci.com/gh/shogo82148/gradle-android-ribbonizer-plugin) [![Download](https://api.bintray.com/packages/shogo82148/maven/ribbonizer-plugin/images/download.svg)](https://bintray.com/shogo82148/maven/ribbonizer-plugin/_latestVersion)

This is a ribbonizer as a Gradle plugin for Android, which adds a ribbon to launcher icons.

[maskarade/gradle-android-ribbonizer-plugin](https://github.com/maskarade/gradle-android-ribbonizer-plugin) is original one,
but it seems to be not maintained.
So, I forked it and published as another plugin.

![](ic-beta.png) ![](ic-debug.png)

## Usage

```groovy
// in build.gradle
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.4.1'
        classpath 'com.shogo82148.ribbonizer:ribbonizer-plugin:3.0.1'
    }
}
```

```groovy
// in app/build.gradle
apply plugin: 'com.shogo82148.ribbonizer'

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
        release {/*non-debuggable build. Will not be ribbonized automatically*/}
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

Copyright (c) 2015 FUJI Goro (gfx).
Copyright (c) 2019 Ichinose Shogo (shogo82148).

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
