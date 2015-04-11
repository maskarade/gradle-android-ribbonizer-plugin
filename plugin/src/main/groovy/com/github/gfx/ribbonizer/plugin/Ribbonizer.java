package com.github.gfx.ribbonizer.plugin;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

public class Ribbonizer {

    final File inputFile;

    final File outputFile;

    final BufferedImage image;

    public Ribbonizer(File inputFile, File outputFile) throws IOException {
        this.inputFile = inputFile;
        this.outputFile = outputFile;

        image = ImageIO.read(inputFile);
    }

    public void save() throws IOException {
        outputFile.getParentFile().mkdirs();
        ImageIO.write(image, "png", outputFile);
    }

    public void process(Stream<Consumer<BufferedImage>> filters) {
        filters.forEach(new Consumer<Consumer<BufferedImage>>() {
            @Override
            public void accept(Consumer<BufferedImage> filter) {
                filter.accept(image);
            }
        });
    }
}