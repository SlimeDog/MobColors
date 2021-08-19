package dev.ratas.mobcolors.coloring.settings;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import org.bukkit.entity.EntityType;

public class AbstractColorMap<T extends Enum<?>> implements ColorMap<T> {
    private final String name;
    private final Map<T, Double> colorMap;
    private final Collection<String> worlds;

    public AbstractColorMap(String name, Map<T, Double> map, Collection<String> worlds) {
        this.name = name;
        this.colorMap = map;
        this.worlds = new HashSet<>(worlds);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getChanceFor(T color) {
        return colorMap.getOrDefault(color, 0.0D);
    }

    @Override
    public ColorChoice<T> rollColor() {
        return ColorChoice.of(this);
    }

    @Override
    public EntityType getApplicableEntityType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<T, Double> getChoices() {
        return Collections.unmodifiableMap(colorMap);
    }

    @Override
    public Collection<String> getApplicableWorlds() {
        return Collections.unmodifiableCollection(worlds);
    }

}
