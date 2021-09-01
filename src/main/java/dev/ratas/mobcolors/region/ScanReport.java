package dev.ratas.mobcolors.region;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class ScanReport<T> {
    private final Map<T, Integer> colors = new HashMap<>();
    private long countedChunks = 0;
    private final Function<Entity, T> colorProvider;
    private final EntityType type;

    public ScanReport(EntityType type, Function<Entity, T> provider) {
        this.type = type;
        this.colorProvider = provider;
    }

    public void count(Entity entity) {
        T color = colorProvider.apply(entity);
        Integer prev = colors.get(color);
        if (prev == null) {
            prev = 0;
        }
        colors.put(color, ++prev);
    }

    public void countAChunk() {
        countedChunks++;
    }

    public EntityType getType() {
        return type;
    }

    public List<Map.Entry<T, Integer>> getSortedColors() {
        List<Map.Entry<T, Integer>> list = new ArrayList<>(colors.entrySet());
        Collections.sort(list, (e1, e2) -> e1.getKey().toString().compareTo(e2.getKey().toString()));
        return list;
    }

    public Map<T, Integer> getColors() {
        return colors;
    }

    public long getChunksCounted() {
        return countedChunks;
    }

}
