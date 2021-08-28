package dev.ratas.mobcolors.config.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.EntityType;

import dev.ratas.mobcolors.coloring.MobColorer;
import dev.ratas.mobcolors.coloring.settings.ColorMap;
import dev.ratas.mobcolors.config.mob.MobSettings;
import dev.ratas.mobcolors.scheduling.abstraction.Scheduler;

public class WorldSettings {
    private final Map<EntityType, ColorMap<?>> entityColorMaps = new EnumMap<>(EntityType.class);
    private final Map<String, ColorMap<?>> colorMapsByName = new HashMap<>(); // useless as it ignores entity type
    private final Map<EntityType, MobColorer<?, ?>> colorers = new EnumMap<>(EntityType.class);

    public void addScheme(ColorMap<?> map, MobSettings settings, Scheduler scheduler) {
        ColorMap<?> prev = entityColorMaps.put(map.getApplicableEntityType(), map);
        if (prev != null && prev != map) {
            entityColorMaps.put(prev.getApplicableEntityType(), prev);
            throw new IllegalArgumentException("Multiple color maps specified for the same world for the entity "
                    + prev.getApplicableEntityType().name() + ": " + map.getName() + " and " + prev.getName());
        }
        colorMapsByName.put(map.getName(), map);
        colorers.put(map.getApplicableEntityType(), ColorerGenerator.generateColorer(map, settings, scheduler));
    }

    public ColorMap<?> getColorMapByEntityType(EntityType type) {
        return entityColorMaps.get(type);
    }

    public ColorMap<?> getColorMapByName(String name) {
        return colorMapsByName.get(name);
    }

    public Collection<ColorMap<?>> getEnabledColorMaps() {
        return new ArrayList<>(entityColorMaps.values());
    }

    public MobColorer<?, ?> getColorer(EntityType type) {
        return colorers.get(type);
    }

    void clear() {
        entityColorMaps.clear();
        colorMapsByName.clear();
        colorers.clear();
    }

}
