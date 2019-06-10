package com.shogo82148.ribbonizer.plugin;

import com.shogo82148.ribbonizer.resource.Resource;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.stream.Stream;

@SuppressWarnings("WeakerAccess")
public class Ribbonizer {

    private final File outputFile;

    private Resource resource;

    public Ribbonizer(Resource resource, File outputFile) {
        this.resource = resource;
        this.outputFile = outputFile;
    }

    public void save() throws IOException {
        if (resource == null) return;
        //noinspection ResultOfMethodCallIgnored
        outputFile.getParentFile().mkdirs();
        resource.save(outputFile);
    }

    public void process(Stream<Consumer<Resource>> filters) {
        if (resource == null) return;
        filters.forEach(filter -> {
            if (filter != null) {
                filter.accept(resource);
            }
        });
    }
}