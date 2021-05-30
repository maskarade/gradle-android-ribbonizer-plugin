package com.shogo82148.ribbonizer.resource

import com.shogo82148.ribbonizer.plugin.Ribbonizer
import java.io.File

abstract class Resource(
    val ribbonizer: Ribbonizer, val file: File) {

    abstract fun apply(filter: Filter)
    abstract fun save(file: File)
}