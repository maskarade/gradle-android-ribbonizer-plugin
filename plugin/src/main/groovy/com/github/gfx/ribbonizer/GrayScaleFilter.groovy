package com.github.gfx.ribbonizer

import groovy.transform.CompileStatic

import java.awt.image.BufferedImage

@CompileStatic
public class GrayScaleFilter implements Ribbonizer.Filter {

    @Override
    void process(BufferedImage inputImage, BufferedImage outputImage) {
        def width = inputImage.width
        def height = inputImage.height

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                def color = inputImage.getRGB(x, y)
                outputImage.setRGB(x, y, toGray(color))
            }
        }
    }

    static int toGray(int color) {
        def a = (color & 0xFF000000)
        def r = (color & 0x00FF0000) >> 16
        def g = (color & 0x0000FF00) >> 8
        def b = (color & 0x000000FF)

        def c = (int) ((2.0 * r + 4.0 * g + b) / 7.0)
        return a | (c << 16) | (c << 8) | c
    }
}