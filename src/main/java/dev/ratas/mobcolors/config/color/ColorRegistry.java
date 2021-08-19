package dev.ratas.mobcolors.config.color;

import java.util.HashMap;
import java.util.Map;

import dev.ratas.mobcolors.coloring.settings.ColorMap;

public class ColorRegistry {
    private final Map<String, ColorMap<?>> colorMaps = new HashMap<>();

    public ColorRegistry() {
        // TODO - add something?
    }

    public void register(String name, ColorMap<?> map) {
        if (isRegistered(name)) {
            throw new IllegalStateException("Color map already registered: " + name);
        }
        colorMaps.put(name, map); // TODO - case insensitive?
    }

    public boolean isRegistered(String name) {
        return getMap(name) != null;
    }

    public ColorMap<?> getMap(String name) {
        return colorMaps.get(name);
    }

    public void clear() {
        colorMaps.clear();
    }

}
