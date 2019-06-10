package com.github.gfx.ribbonizer.resource;

public interface Filter {
    void apply(ImageIcon icon);
    void apply(ImageAdaptiveIcon icon);
}