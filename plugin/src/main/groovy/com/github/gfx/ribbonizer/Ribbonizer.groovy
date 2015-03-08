package com.github.gfx.ribbonizer

import groovy.transform.CompileStatic

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

@CompileStatic
public class Ribbonizer {

    final File inputFile;

    final File outputFile;

    final BufferedImage inputImage;

    final BufferedImage outputImage;

    public Ribbonizer(File inputFile, File outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;

        inputImage = ImageIO.read(inputFile)
        outputImage =
                new BufferedImage(inputImage.width, inputImage.height, BufferedImage.TYPE_INT_ARGB);
    }

    public void save() {
        outputFile.getParentFile().mkdirs()
        ImageIO.write(outputImage, "png", outputFile)
    }

    int toGray(int color) {
        def a = (color & 0xFF000000)
        def r = (color & 0x00FF0000) >> 16
        def g = (color & 0x0000FF00) >> 8
        def b = (color & 0x000000FF)

        def c = (int) ((2.0 * r + 4.0 * g + b) / 7.0)
        return a | (c << 16) | (c << 8) | c
    }

    public Ribbonizer makeGrayScale() {
        def width = inputImage.width
        def height = inputImage.height

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                def color = inputImage.getRGB(x, y)
                outputImage.setRGB(x, y, toGray(color))
            }
        }
    }


    void addRibbon() {

    }
}