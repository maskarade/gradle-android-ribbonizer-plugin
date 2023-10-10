plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.shogo82148.ribbonizer")
}

android {
    compileSdk = 33
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "com.shogo82148.ribbonizer.example"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

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
    namespace = "com.shogo82148.ribbonizer.example"
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
            variant.buildType == "debug" -> {
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

val kotlinVersion = properties["kotlin_version"] as String

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
}
