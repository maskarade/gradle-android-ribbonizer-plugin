# Ribbonizer plugin for Android

[![test](https://github.com/shogo82148/gradle-android-ribbonizer-plugin/actions/workflows/test.yml/badge.svg)](https://github.com/shogo82148/gradle-android-ribbonizer-plugin/actions/workflows/test.yml)

This is a ribbonizer as a Gradle plugin for Android, which adds a ribbon to launcher icons.

[maskarade/gradle-android-ribbonizer-plugin](https://github.com/maskarade/gradle-android-ribbonizer-plugin) is original one,
but it seems to be not maintained.
So, I forked it and published as another plugin.

![](ic-beta.png) ![](ic-debug.png)

## Usage

### Groovy Script

```groovy
// in build.gradle
buildscript {
    repositories {
        // for the Android Gradle plugin
        google()

        // for the ribbonizer plugin
        mavenCentral()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:7.0.3'
        classpath 'com.shogo82148.ribbonizer:ribbonizer-plugin:3.1.0'
    }
}
```

```groovy
// in app/build.gradle
// the full example of build.gradle is in example/custom
plugins {
    id 'com.android.application'
    id 'com.shogo82148.ribbonizer'
}

android {
    // ...(snip)...

    buildTypes {
        debug {
            // debuggable build, which will ribbonized automatically.
        }
        beta {
            // debuggable build which will automatically ribbonized.
            debuggable true
        }
        canary {
            // non-debuggable build which will no automatically ribbonized.
            // But, we force one of its flavors. See `ribbonizer` for how-to
            debuggable false
        }
        release {
            // non-debuggable build. Will not be ribbonized automatically.
        }
    }

    flavorDimensions 'flavor'
    productFlavors {
        local {
            dimension 'flavor'
        }
        qa {
            dimension 'flavor'
        }
        staging {
            dimension 'flavor'
        }
        production {
            dimension 'flavor'
        }
    }
}

ribbonizer {
    // additional icons for ribbinizing
    // "manifest application[android:icon]" and "manifest application[android:round_icon]" are automatically added to the list
    iconNames "@drawable/dog", "@drawable/thinking"

    builder { variant, iconFile ->
        if (variant.flavorName == "local") {
            // change ribbon colors by product flavors
            return grayRibbonFilter(variant, iconFile)
        } else if (variant.flavorName == "qa") {
            // customColorRibbonFilter allows setting any color code
            def filter = customColorRibbonFilter(variant, iconFile, "#00C89C")
            // Finer control of the label text can be achieved by setting it manually, or set to
            // null for an unlabelled ribbon. The default is to use the flavor name.
            filter.label = "QA" + variant.versionCode
            filter.largeRibbon = (iconFile.name == "ic_launcher.png")
            return filter
        } else if (variant.buildType.name == "debug") {
            if (variant.flavorName == "production") {
                // Particular configurations can be skipped by returning no filters
                return null
            } else {
                // Other filters can be applied, as long as they implement Consumer<BufferedImage>
                return grayScaleFilter(variant, iconFile)
            }
        } else {
            // the default configure of ribbons
            return greenRibbonFilter(variant, iconFile)
        }
    }

    // Although `canary` build-type is marked as `non-debuggable`
    // we can still force specific variants to be ribbonized:
    forcedVariantsNames "localCanary"
}
```

### Kotlin Script

```kotlin
// in build.gradle.kts
buildscript {
    repositories {
        // for the Android Gradle plugin
        google()

        // for the ribbonizer plugin
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.3")
        classpath("com.shogo82148.ribbonizer:ribbonizer-plugin:3.1.0")
    }
}
```

```kotlin
// in app/build.gradle.kts
// the full example of build.gradle.kts is in example/custom-kts
plugins {
    id("com.android.application")
    id("com.shogo82148.ribbonizer")
}

android {
    // ...(snip)...

    buildTypes {
        getByName("debug") {
            // debuggable build, which will ribbonized automatically.
        }
        create("beta") {
            // debuggable build which will automatically ribbonized.
            isDebuggable = true
        }
        create("canary") {
            // non-debuggable build which will no automatically ribbonized.
            // But, we force one of its flavors. See `ribbonizer` for how-to
            isDebuggable = false
        }
        getByName("release") {
            // non-debuggable build. Will not be ribbonized automatically.
        }
    }

    flavorDimensions.add("flavor")
    productFlavors {
        create("local") {
            dimension = "flavor"
        }
        create("qa") {
            dimension = "flavor"
        }
        create("staging") {
            dimension = "flavor"
        }
        create("production") {
            dimension = "flavor"
        }
    }
}

ribbonizer {
    // additional icons for ribbinizing
    // "manifest application[android:icon]" and "manifest application[android:round_icon]" are automatically added to the list
    iconNames("@drawable/dog", "@drawable/thinking")

    builder { variant, iconFile ->
        when {
            variant.flavorName == "local" -> {
                // change ribbon colors by product flavors
                grayRibbonFilter(variant, iconFile)
            }
            variant.flavorName == "qa" -> {
                // customColorRibbonFilter allows setting any color code
                val filter = customColorRibbonFilter(variant, iconFile, "#00C89C")
                // Finer control of the label text can be achieved by setting it manually, or set to
                // null for an unlabelled ribbon. The default is to use the flavor name.
                filter.label = "QA" + variant.versionCode
                filter.largeRibbon = (iconFile.name == "ic_launcher.png")
                filter
            }
            variant.buildType.name == "debug" -> {
                if (variant.flavorName == "production") {
                    // Particular configurations can be skipped by returning no filters
                    null
                } else {
                    // Other filters can be applied, as long as they implement Consumer<BufferedImage>
                    grayScaleFilter(variant, iconFile)
                }
            }
            else -> {
                // the default configure of ribbons
                greenRibbonFilter(variant, iconFile)
            }
        }
    }

    // Although `canary` build-type is marked as `non-debuggable`
    // we can still force specific variants to be ribbonized:
    forcedVariantsNames("localCanary")
}
```

## Project Structure

```
ribbonizer-plugin/ - The main module of a Gradle plugin
examples/ - Examples of android applications that use this plugin
```

You can test this project with `make test`.

## Release Engineering

Edit the version number in `ribbonizer-plugin/build.gradle.kts`.

```kotlin
group = "com.shogo82148.ribbonizer"
version = "x.y.z"
```

and run `make publish`.

```console
make publish # upload artifacts to Maven Central
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
