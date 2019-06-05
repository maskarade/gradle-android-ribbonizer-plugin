package com.github.gfx.ribbonizer.plugin;

import com.github.gfx.ribbonizer.resource.ImageIcon;
import com.github.gfx.ribbonizer.resource.Resource;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

public class Ribbonizer {

    final File inputFile;

    final File outputFile;

    ImageIcon icon;

    public Ribbonizer(File inputFile, File outputFile) throws IOException {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        icon = new ImageIcon(inputFile);
    }

    public void save() throws IOException {
        outputFile.getParentFile().mkdirs();
        ImageIO.write(icon.getImage(), "png", outputFile);
    }

    public void process(Stream<Consumer<Resource>> filters) {
        filters.forEach(filter -> {
            if (filter != null) {
                filter.accept(icon);
            }
        });
    }
}