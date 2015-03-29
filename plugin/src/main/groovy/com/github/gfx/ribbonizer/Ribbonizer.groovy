package com.github.gfx.ribbonizer

import groovy.transform.CompileStatic
import org.gradle.api.Action

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

@CompileStatic
public class Ribbonizer {

    final File inputFile

    final File outputFile

    final BufferedImage image

    public Ribbonizer(File inputFile, File outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;

        image = ImageIO.read(inputFile)
    }

    public void save() {
        outputFile.getParentFile().mkdirs()
        ImageIO.write(image, "png", outputFile)
    }

    public void process(List<Action<BufferedImage>> filters) {
        filters.forEach { Action<BufferedImage> filter ->
            filter.execute(image)
        }
    }
}