package com.shogo82148.ribbonizer.resource;

public interface Filter {
    void apply(ImageIcon icon);
    void apply(ImageAdaptiveIcon icon);
}
