package dev.ratas.mobcolors.region;

import org.bukkit.World;
import org.bukkit.entity.Entity;

public class RectangularRegionInfo extends AbstractRegionInfo {
    private final int xLength;
    private final int zLength;

    public RectangularRegionInfo(World world, int regionX, int regionZ, boolean ignoredUngenerated) {
        this(world, regionX << 5, regionZ << 5, (regionX << 5) + 31, (regionZ << 5) + 31, ignoredUngenerated);
    }

    public RectangularRegionInfo(World world, int startChunkX, int startChunkZ, int stopChunkX, int stopChunkZ,
            boolean ignoreUngenerated) {
        super(world, startChunkX, startChunkZ, ignoreUngenerated);
        this.xLength = stopChunkX + 1 - getStartChunkX();
        this.zLength = stopChunkZ + 1 - getStartChunkZ();
    }

    @Override
    public long getNumberOfTotalChunks() {
        return xLength * zLength;
    }

    @Override
    public boolean isInRange(Entity entity) {
        return true; // always in range in this case
    }

    @Override
    public int getChunkXFor(long order) {
        if (order < 0 || order > getNumberOfTotalChunks()) {
            throw new IllegalArgumentException(
                    "Unknown order: " + order + " (out of " + getNumberOfTotalChunks() + ")");
        }
        int xIncrements = (int) order % xLength;
        return getStartChunkX() + xIncrements;
    }

    @Override
    public int getChunkZFor(long order) {
        if (order < 0 || order > getNumberOfTotalChunks()) {
            throw new IllegalArgumentException(
                    "Unknown order: " + order + " (out of " + getNumberOfTotalChunks() + ")");
        }
        int zIncrements = (int) order / xLength;
        return getStartChunkZ() + zIncrements;
    }

}
