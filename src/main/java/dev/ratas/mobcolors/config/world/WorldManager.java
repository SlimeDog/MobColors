package dev.ratas.mobcolors.config.world;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.World;

import dev.ratas.mobcolors.coloring.settings.ColorMap;
import dev.ratas.mobcolors.config.mob.MobSettings;
import dev.ratas.mobcolors.scheduling.abstraction.Scheduler;

public class WorldManager {
    private final Map<String, WorldSettings> worldSettings = new HashMap<>();
    private final WorldSettings defaultWorldSettings = new WorldSettings();

    public void setAllUsedWorlds(List<String> worldNames) {
        for (String world : worldNames) {
            worldSettings.put(world.toLowerCase(), new WorldSettings());
        }
    }

    public void addMobSettings(MobSettings settings, Scheduler scheduler, Logger logger) {
        Set<String> allWorlds = new HashSet<>(worldSettings.keySet());
        for (ColorMap<?> map : settings.getAllColorMaps()) {
            for (String worldName : map.getApplicableWorlds()) {
                WorldSettings ws = worldSettings.get(worldName.toLowerCase());
                try {
                    ws.addScheme(map, settings, scheduler);
                } catch (IllegalArgumentException e) {
                    logger.warning("Problem setting color scheme for world " + worldName + " : " + e.getMessage());
                    continue;
                }
                allWorlds.remove(worldName.toLowerCase());
            }
            if (map.getName().equals("default")) {
                defaultWorldSettings.addScheme(map, settings, scheduler);
            }
        }
        // refer to defaults in all worlds not named within a color map
        ColorMap<?> defaultMap = settings.getDefaultColorMap();
        if (defaultMap == null) {
            return; // no defaults set
        }
        for (String unNamedWorld : allWorlds) {
            WorldSettings ws = worldSettings.get(unNamedWorld.toLowerCase());
            ws.addScheme(defaultMap, settings, scheduler);
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
