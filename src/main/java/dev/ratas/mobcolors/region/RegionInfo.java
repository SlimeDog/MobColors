package dev.ratas.mobcolors.region;

import org.bukkit.World;
import org.bukkit.entity.Entity;

public interface RegionInfo {

    int getStartChunkX();

    int getStartChunkZ();

    long getNumberOfTotalChunks();

    boolean shouldIgnoreUngenerated();

    World getWorld();

    boolean isInRange(Entity entity);

    int getChunkXFor(long order);

    int getChunkZFor(long order);

}
