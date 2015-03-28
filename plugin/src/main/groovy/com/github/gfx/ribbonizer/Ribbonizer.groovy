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

    public void process(List<Filter> filters) {
        filters.forEach { Filter filter ->
            filter.process(inputImage, outputImage);
        }
    }

    public interface Filter {
        void process(BufferedImage inputImage, BufferedImage outputImage);
    }
}