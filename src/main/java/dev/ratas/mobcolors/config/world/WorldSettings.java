package dev.ratas.mobcolors.config.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.EntityType;

import dev.ratas.mobcolors.coloring.MobColorer;
import dev.ratas.mobcolors.coloring.settings.ColorMap;
import dev.ratas.mobcolors.config.mob.MobSettings;
import dev.ratas.mobcolors.config.mob.MobType;
import dev.ratas.mobcolors.scheduling.abstraction.Scheduler;

public class WorldSettings {
    private final Map<EntityType, ColorMap<?>> entityColorMaps = new EnumMap<>(EntityType.class);
    private final Map<String, ColorMap<?>> colorMapsByName = new HashMap<>(); // useless as it ignores entity type
    private final Map<MobType, MobColorer<?, ?>> colorers = new EnumMap<>(MobType.class);

    public void addScheme(ColorMap<?> map, MobSettings settings, Scheduler scheduler) {
        ColorMap<?> prev = entityColorMaps.put(map.getApplicableEntityType().getEntityType(), map);
        if (prev != null && prev != map) {
            entityColorMaps.put(prev.getApplicableEntityType().getEntityType(), prev);
            throw new IllegalArgumentException("Multiple color maps specified for the same world for the entity "
                    + prev.getApplicableEntityType().name() + ": " + map.getName() + " and " + prev.getName());
        }
        colorMapsByName.put(map.getName(), map);
        colorers.put(map.getApplicableEntityType(), ColorerGenerator.generateColorer(map, settings, scheduler));
        if (map.getApplicableEntityType() == MobType.llama) {
            entityColorMaps.put(EntityType.TRADER_LLAMA, map);
        }
    }

    public ColorMap<?> getColorMapByEntityType(EntityType type) {
        return entityColorMaps.get(type);
    }

    public ColorMap<?> getColorMapByName(String name) {
        return colorMapsByName.get(name);
    }

    public List<ColorMap<?>> getEnabledColorMaps(boolean sorted) {
        List<ColorMap<?>> list = new ArrayList<>(entityColorMaps.values());
        if (sorted) {
            Collections.sort(list,
                    (m1, m2) -> m1.getApplicableEntityType().name().compareTo(m2.getApplicableEntityType().name()));
        }
        return list;
    }

    public MobColorer<?, ?> getColorer(EntityType type) {
        MobType mobType = MobType.getType(type);
        if (mobType == null) {
            return null;
        }
        return colorers.get(mobType);
    }

    void clear() {
        entityColorMaps.clear();
        colorMapsByName.clear();
        colorers.clear();
    }

}
