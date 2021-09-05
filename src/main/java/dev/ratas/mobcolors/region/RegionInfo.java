package dev.ratas.mobcolors.region;

import org.bukkit.World;
import org.bukkit.entity.Entity;

import dev.ratas.mobcolors.utils.WorldDescriptor;

public interface RegionInfo {

    int getStartChunkX();

    int getStartChunkZ();

    long getNumberOfTotalChunks();

    boolean shouldIgnoreUngenerated();

    World getWorld();

    default WorldDescriptor getWorldDescriptor() {
        return WorldDescriptor.wrap(getWorld());
    }

    boolean isInRange(Entity entity);

    int getChunkXFor(long order);

    int getChunkZFor(long order);

}
