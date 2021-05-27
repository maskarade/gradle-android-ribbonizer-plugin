package com.shogo82148.ribbonizer.plugin

import com.shogo82148.ribbonizer.resource.Resource
import java.io.File
import java.util.function.Consumer
import java.util.stream.Stream

class Ribbonizer (
    private var resource: Resource,
    var outputFile: File
) {

    fun save() {
        outputFile.parentFile.mkdirs();
        resource.save(outputFile);
    }

    fun process(filters: Stream<Consumer<Resource>>) {
        filters.forEach {
            it.accept(resource)
        }
    }
}