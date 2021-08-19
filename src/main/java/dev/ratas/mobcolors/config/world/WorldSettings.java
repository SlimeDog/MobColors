package dev.ratas.mobcolors.config.world;

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
    private final Map<String, ColorMap<?>> colorMapsByName = new HashMap<>();
    private final Map<EntityType, MobColorer<?, ?>> colorers = new EnumMap<>(EntityType.class);

    public void addScheme(ColorMap<?> map, MobSettings settings, Scheduler scheduler) {
        entityColorMaps.put(map.getApplicableEntityType(), map);
        colorMapsByName.put(map.getName(), map);
        colorers.put(map.getApplicableEntityType(), ColorerGenerator.generateColorer(map, settings, scheduler));
    }

    public ColorMap<?> getColorMapByEntityType(EntityType type) {
        return entityColorMaps.get(type);
    }

    public ColorMap<?> getColorMapByName(String name) {
        return colorMapsByName.get(name);
    }

    public MobColorer<?, ?> getColorer(EntityType type) {
        return colorers.get(type);
    }

}
