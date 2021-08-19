package dev.ratas.mobcolors.config.mob;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.EntityType;

import dev.ratas.mobcolors.coloring.settings.ColorMap;

public class MobSettings {
    private final EntityType type;
    private final Map<String, ColorMap<?>> colorMaps;
    private final ColorMap<?> defaultColorMap;
    private final boolean includeLeashed;
    private final boolean includePets;

    public MobSettings(EntityType type, Map<String, ColorMap<?>> colorMaps, boolean includeLeashed,
            boolean includePets) {
        this.type = type;
        this.colorMaps = new HashMap<>(colorMaps);
        this.defaultColorMap = colorMaps.get("default");
        this.includeLeashed = includeLeashed;
        this.includePets = includePets;
    }

    public EntityType getEntityType() {
        return type;
    }

    public ColorMap<?> getColorMap(String name) {
        return colorMaps.get(name);
    }

    /**
     * Returns the default ColorMap or null if one is not specified.
     *
     * @return
     */
    public ColorMap<?> getDefaultColorMap() {
        return defaultColorMap;
    }

    public boolean shouldIncludeLeashed() {
        return includeLeashed;
    }

    public boolean shouldIncludePets() {
        return includePets;
    }

    public Collection<ColorMap<?>> getAllColorMaps() {
        return Collections.unmodifiableCollection(colorMaps.values());
    }

}
