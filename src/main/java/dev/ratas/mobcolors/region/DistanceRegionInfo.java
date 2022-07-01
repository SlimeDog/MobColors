package dev.ratas.mobcolors.region;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class DistanceRegionInfo extends AbstractRegionInfo {
    private final Location centerAtY0;
    private final double maxDistance;
    private final double maxDistanceSquared;
    private final int[] chunkXCoords;
    private final int[] chunkZCoords;

    public DistanceRegionInfo(Location center, double distance, boolean ignoreUngenerated) {
        this(center, distance, new CoordsProvider(center, distance), ignoreUngenerated);
    }

    private DistanceRegionInfo(Location center, double distance, CoordsProvider coordsProvider,
            boolean ignoreUngenerated) {
        super(center.getWorld(), coordsProvider.xCoords[0], coordsProvider.zCoords[0], ignoreUngenerated);
        this.centerAtY0 = center.clone();
        this.centerAtY0.setY(0.0D);
        this.chunkXCoords = coordsProvider.xCoords;
        this.chunkZCoords = coordsProvider.zCoords;
        this.maxDistance = distance;
        this.maxDistanceSquared = distance * distance;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public double getMaxDistanceSquared() {
        return maxDistanceSquared;
    }

    @Override
    public long getNumberOfTotalChunks() {
        return chunkXCoords.length;
    }

    @Override
    public boolean isInRange(Entity entity) {
        Location entityLoc = entity.getLocation();
        entityLoc.setY(0.0D);
        return entityLoc.distanceSquared(centerAtY0) <= maxDistanceSquared;
    }

    @Override
    public int getChunkXFor(long order) {
        if (order < 0 || order >= getNumberOfTotalChunks()) {
            throw new IllegalArgumentException(
                    "Unknown order: " + order + " (out of " + getNumberOfTotalChunks() + ")");
        }
        return chunkXCoords[(int) order];
    }

    @Override
    public int getChunkZFor(long order) {
        if (order < 0 || order >= getNumberOfTotalChunks()) {
            throw new IllegalArgumentException(
                    "Unknown order: " + order + " (out of " + getNumberOfTotalChunks() + ")");
        }
        return chunkZCoords[(int) order];
    }

    private static class CoordsProvider {
        private final int[] xCoords;
        private final int[] zCoords;

        private CoordsProvider(Location center, double distance) {
            center = center.clone();
            center.setY(0.0D);
            int centerX = center.getBlockX();
            int centerZ = center.getBlockZ();
            int centerChunkX = centerX >> 4;
            int centerChunkZ = centerZ >> 4;
            int chunkDistance = ((int) distance) >> 4;
            double distance2 = distance * distance;
            List<Integer> coordsX = new ArrayList<>();
            List<Integer> coordsZ = new ArrayList<>();
            for (int chunkX = centerChunkX - chunkDistance; chunkX <= centerChunkX + chunkDistance; chunkX++) {
                for (int chunkZ = centerChunkZ - chunkDistance; chunkZ <= centerChunkZ + chunkDistance; chunkZ++) {
                    if (isWithinRange(centerX, centerZ, chunkX, chunkZ, distance2)
                            || (chunkX == centerChunkX && chunkZ == centerChunkZ)) {
                        coordsX.add(chunkX);
                        coordsZ.add(chunkZ);
                    }
                }
            }
            this.xCoords = new int[coordsX.size()];
            this.zCoords = new int[coordsZ.size()];
            for (int i = 0; i < xCoords.length; i++) {
                this.xCoords[i] = coordsX.get(i);
                this.zCoords[i] = coordsZ.get(i);
            }
        }

        private static boolean isWithinRange(int centerX, int centerZ, int chunkX, int chunkZ, double squaredDistance) {
            int chunkMinX = chunkX << 4;
            int chunkMaxX = chunkMinX + 15;
            int chunkMinZ = chunkZ << 4;
            int chunkMaxZ = chunkMinZ + 15;
            // if one corner of chunk in range, then consider it in range
            return isBlockWithinRange(centerX, centerZ, chunkMinX, chunkMinZ, squaredDistance)
                    || isBlockWithinRange(centerX, centerZ, chunkMinX, chunkMaxZ, squaredDistance)
                    || isBlockWithinRange(centerX, centerZ, chunkMaxX, chunkMinZ, squaredDistance)
                    || isBlockWithinRange(centerX, centerZ, chunkMaxX, chunkMaxZ, squaredDistance);
        }

        private static boolean isBlockWithinRange(int centerX, int centerZ, int blockX, int blockZ,
                double squaredDistance) {
            int diffX = blockX - centerX;
            int diffZ = blockZ - centerZ;
            return diffX * diffX + diffZ * diffZ < squaredDistance;
        }
    }

}
