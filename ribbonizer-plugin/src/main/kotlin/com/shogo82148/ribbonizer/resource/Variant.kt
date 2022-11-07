package com.shogo82148.ribbonizer.resource

@Suppress("unused") // The fields of the Variant are used from Gradle DSL.
class Variant(
    val name: String,
    val debuggable: Boolean,
    val buildType: String,
    val versionCode: Int,
    val versionName: String,
    val flavorName: String
) {
}