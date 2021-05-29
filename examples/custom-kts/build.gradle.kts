plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.shogo82148.ribbonizer")
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.3")

    defaultConfig {
        applicationId = "com.shogo82148.ribbonizer.example"
        minSdkVersion(30)
        targetSdkVersion(30)
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
        getByName("debug") {}
        create("beta") { debuggable(true) }
        create("canary") { debuggable(true) }
        getByName("release") {}
    }

    flavorDimensions("flavor")
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
    iconNames("@drawable/dog", "@drawable/thinking")
    forcedVariantsNames("localCanary")
    builder { variant, iconFile ->
        when {
            variant.flavorName == "local" -> {
                grayRibbonFilter(variant, iconFile)
            }
            variant.flavorName == "qa" -> {
                val filter = customColorRibbonFilter(variant, iconFile, "#00C89C")
                filter.label = "QA" + variant.versionCode
                filter.largeRibbon = (iconFile.name == "ic_launcher.png")
                filter
            }
            variant.buildType.name == "debug" -> {
                if (variant.flavorName == "production") {
                    null
                } else {
                    customColorRibbonFilter(variant, iconFile, "#0000FF")
                }
            }
            else -> {
                greenRibbonFilter(variant, iconFile)
            }
        }
    }
}

val kotlinVersion = properties["kotlin_version"] as String

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("androidx.core:core-ktx:1.5.0")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
}