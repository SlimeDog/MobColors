package dev.ratas.mobcolors.region;

import org.bukkit.World;

public abstract class AbstractRegionInfo implements RegionInfo {
    private final World world;
    private final int startChunkX;
    private final int startChunkZ;
    private final boolean ignoredUngenerated;

    protected AbstractRegionInfo(World world, int startChunkX, int startChunkZ, boolean ignoreUngenerated) {
        this.world = world;
        this.startChunkX = startChunkX;
        this.startChunkZ = startChunkZ;
        this.ignoredUngenerated = ignoreUngenerated;
    }

    @Override
    public int getStartChunkX() {
        return startChunkX;
    }

    @Override
    public int getStartChunkZ() {
        return startChunkZ;
    }

    @Override
    public boolean shouldIgnoreUngenerated() {
        return ignoredUngenerated;
    }

    @Override
    public World getWorld() {
        return world;
    }

}
