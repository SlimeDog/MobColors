package dev.ratas.mobcolors.region;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.DyeColor;
import org.bukkit.entity.Sheep;

public class ScanReport {
    private final Map<DyeColor, Integer> colors = new HashMap<>();
    private long countedChunks = 0;

    public void count(Sheep sheep) {
        DyeColor color = sheep.getColor();
        Integer prev = colors.get(color);
        if (prev == null) {
            prev = 0;
        }
        colors.put(color, ++prev);
    }

    public void countAChunk() {
        countedChunks++;
    }

    public Map<DyeColor, Integer> getColors() {
        return colors;
    }

    public long getChunksCounted() {
        return countedChunks;
    }

}
