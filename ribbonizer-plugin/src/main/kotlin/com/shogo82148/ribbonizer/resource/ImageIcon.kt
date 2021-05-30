package com.shogo82148.ribbonizer.resource

import com.shogo82148.ribbonizer.plugin.Ribbonizer
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

// ImageIcon is a raster type icon
class ImageIcon(ribbonizer: Ribbonizer, file: File) : Resource(ribbonizer, file) {
    val image: BufferedImage = ImageIO.read(file)

    override fun apply(filter: Filter) {
        filter.apply(this)
    }

    override fun save(file: File) {
        ImageIO.write(image, "png", file)
    }
}