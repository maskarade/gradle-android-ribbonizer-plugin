package com.shogo82148.ribbonizer.resource

import java.io.File

abstract class Resource {
    abstract fun apply(filter: Filter)
    abstract fun save(file: File)
}