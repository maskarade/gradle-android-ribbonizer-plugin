package com.github.gfx.ribbonizer.plugin;

import com.github.gfx.ribbonizer.resource.ImageIcon;
import com.github.gfx.ribbonizer.resource.Resource;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

@SuppressWarnings("WeakerAccess")
public class Ribbonizer {

    private final File outputFile;

    private ImageIcon icon;

    public Ribbonizer(File inputFile, File outputFile) throws IOException {
        this.outputFile = outputFile;
        icon = new ImageIcon(inputFile);
    }

    public void save() throws IOException {
        //noinspection ResultOfMethodCallIgnored
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