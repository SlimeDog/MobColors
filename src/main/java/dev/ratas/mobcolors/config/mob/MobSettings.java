package dev.ratas.mobcolors.config.mob;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import dev.ratas.mobcolors.coloring.settings.ColorMap;
import dev.ratas.mobcolors.region.RegionOptions;

public class MobSettings {
    private final MobType type;
    private final Map<String, ColorMap<?>> colorMaps;
    private final ColorMap<?> defaultColorMap;
    private final boolean includeLeashed;
    private final boolean includePets;
    private final boolean includeTraders;

    public MobSettings(MobType type, Map<String, ColorMap<?>> colorMaps, boolean includeLeashed, boolean includePets,
            boolean includeTraders) {
        this(type, colorMaps, includeLeashed, includePets, includeTraders, true);
    }

    private MobSettings(MobType type, Map<String, ColorMap<?>> colorMaps, boolean includeLeashed,
            boolean includePets, boolean includeTraders, boolean wrapMap) {
        this.type = type;
        this.colorMaps = wrapMap ? new HashMap<>(colorMaps) : colorMaps;
        this.defaultColorMap = colorMaps.get("default");
        this.includeLeashed = includeLeashed;
        this.includePets = includePets;
        this.includeTraders = includeTraders;
    }

    public MobType getEntityType() {
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

    public boolean shouldIncludeTraders() {
        return includeTraders;
    }

    public Collection<ColorMap<?>> getAllColorMaps() {
        return Collections.unmodifiableCollection(colorMaps.values());
    }

    public MobSettings wrapWith(RegionOptions options) {
        return new MobSettingsWrapper(this, !options.shouldIgnoreLeashed(), !options.shouldIgnorePets(),
                !options.shouldIgnoreTraders());
    }

    private final class MobSettingsWrapper extends MobSettings {

        private MobSettingsWrapper(MobSettings delegate, Boolean includeLeashed, Boolean includePets,
                Boolean includeTraders) {
            super(delegate.type, delegate.colorMaps, includeLeashed != null ? includeLeashed : delegate.includeLeashed,
                    includePets != null ? includePets : delegate.includePets,
                    includeTraders != null ? includeTraders : delegate.includeTraders, false);
        }

    }

}
