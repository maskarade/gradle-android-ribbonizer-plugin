package com.github.gfx.ribbonizer;

import org.gradle.api.Action;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

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

    public void process(List<Action<BufferedImage>> filters) {
        filters.forEach((Action<BufferedImage> filter) -> {
                filter.execute(image);
        });
    }
}