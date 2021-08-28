package dev.ratas.mobcolors.config.world;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.World;

import dev.ratas.mobcolors.coloring.settings.ColorMap;
import dev.ratas.mobcolors.config.mob.MobSettings;
import dev.ratas.mobcolors.scheduling.abstraction.Scheduler;

public class WorldManager {
    private final Map<String, WorldSettings> worldSettings = new HashMap<>();
    private final WorldSettings defaultWorldSettings = new WorldSettings();

    public void addMobSettings(MobSettings settings, Scheduler scheduler) {
        for (ColorMap<?> map : settings.getAllColorMaps()) {
            for (String worldName : map.getApplicableWorlds()) {
                WorldSettings ws = worldSettings.computeIfAbsent(worldName.toLowerCase(), (n) -> new WorldSettings());
                ws.addScheme(map, settings, scheduler);
            }
            if (map.getName().equals("default")) {
                defaultWorldSettings.addScheme(map, settings, scheduler);
            }
        }
    }

    public WorldSettings getWorldSettings(World world) {
        return getWorldSettings(world.getName());
    }

    public WorldSettings getWorldSettings(String worldName) {
        return worldSettings.getOrDefault(worldName.toLowerCase(), defaultWorldSettings);
    }

    public void clear() {
        worldSettings.clear();
    }

}
