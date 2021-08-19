package dev.ratas.mobcolors.coloring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.EntityType;

public class ColorerManager {
    private final Map<EntityType, List<MobColorer<?, ?>>> colorers = new HashMap<>();

    public void registerColorer(EntityType type, MobColorer<?, ?> colorer) {
        List<MobColorer<?, ?>> perClass = colorers.computeIfAbsent(type, (k) -> new ArrayList<>());
        perClass.add(colorer);
    }

    public Collection<MobColorer<?, ?>> getColorersFor(EntityType type) {
        List<MobColorer<?, ?>> perClass = colorers.get(type);
        return perClass == null ? Collections.emptyList() : new ArrayList<>(perClass);
    }

}
