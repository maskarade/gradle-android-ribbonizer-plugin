package com.shogo82148.ribbonizer

import com.android.build.gradle.api.ApplicationVariant
import com.shogo82148.ribbonizer.resource.Resource
import org.gradle.api.HasImplicitReceiver
import java.io.File
import java.util.function.BiFunction
import java.util.function.Consumer

interface FilterBuilder: BiFunction<ApplicationVariant, File, Consumer<Resource>?> {
}