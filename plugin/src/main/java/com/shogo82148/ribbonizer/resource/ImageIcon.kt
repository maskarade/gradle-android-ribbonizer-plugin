package com.shogo82148.ribbonizer.resource

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class ImageIcon(private val file: File) : Resource() {
    val image: BufferedImage = ImageIO.read(file)
    
    override fun apply(filter: Filter) {
        filter.apply(this)
    }

    override fun save(dir: File) {
        ImageIO.write(image, "png", dir)
    }
}
