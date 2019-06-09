package com.github.gfx.ribbonizer.plugin;

import com.github.gfx.ribbonizer.resource.AdaptiveIcon;
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

    private Resource icon;

    public Ribbonizer(File inputFile, File outputFile) throws Exception {
        this.outputFile = outputFile;
        if (inputFile.getName().endsWith(".png")) {
            icon = new ImageIcon(inputFile);
        }
        if (inputFile.getName().endsWith(".xml")) {
            icon = new AdaptiveIcon(inputFile);
        }
    }

    public void save() throws IOException {
        if (icon == null) return;
        //noinspection ResultOfMethodCallIgnored
        outputFile.getParentFile().mkdirs();
        icon.save(outputFile);
    }

    public void process(Stream<Consumer<Resource>> filters) {
        if (icon == null) return;
        filters.forEach(filter -> {
            if (filter != null) {
                filter.accept(icon);
            }
        });
    }
}