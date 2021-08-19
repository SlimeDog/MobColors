package dev.ratas.mobcolors.coloring.settings;

import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public enum ColorReason {
    SPAWN, OTHER;

    public static ColorReason fromSpawnReason(SpawnReason reason) {
        return (reason == SpawnReason.DEFAULT) ? SPAWN : OTHER;

    }

}
