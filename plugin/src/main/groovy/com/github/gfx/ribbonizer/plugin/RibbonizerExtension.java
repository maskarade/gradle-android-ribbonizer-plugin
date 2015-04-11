package com.github.gfx.ribbonizer.plugin;

import com.github.gfx.ribbonizer.FilterBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class RibbonizerExtension {
    public static String NAME = "ribbonizer";

    Set<String> iconNames = new HashSet<>();
    List<FilterBuilder> filterBuilders = new ArrayList<>();

    public RibbonizerExtension() {
    }

    public Set<String> getIconNames() {
        return iconNames;
    }

    public void setIconNames(List<String> iconNames) {
        this.iconNames = new HashSet<>(iconNames);
    }

    public void addIconName(String name) {
        iconNames.add(name);
    }

    public List<FilterBuilder> getFilterBuilders() {
        return filterBuilders;
    }

    public void setFilterBuilders(Collection<FilterBuilder> filterBuilders) {
        this.filterBuilders = new ArrayList<>(filterBuilders);
    }

    public void builder(Class<? extends FilterBuilder> builderClass)
            throws IllegalAccessException, InstantiationException {
        this.filterBuilders.clear();
        this.filterBuilders.add(builderClass.newInstance());
    }
}