package com.shogo82148.ribbonizer.resource;

import java.io.File;
import java.io.IOException;

public abstract class Resource {
    public abstract void apply(Filter filter);
    public abstract void save(File dir) throws IOException;
}
