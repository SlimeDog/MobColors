package dev.ratas.mobcolors.region.version;

import java.util.Objects;
import java.util.UUID;

import org.bukkit.Chunk;

public class ChunkInfo {
    private static final String UNKNOWN_WORLD_NAME = "N/A";
    private final UUID worldId;
    private final int chunkX;
    private final int chunkZ;
    private final String worldName;

    public ChunkInfo(UUID worldId, int chunkX, int chunkZ) {
        this(worldId, chunkX, chunkZ, UNKNOWN_WORLD_NAME);
    }

    public ChunkInfo(UUID worldId, int chunkX, int chunkZ, String worldName) {
        this.worldId = worldId;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.worldName = worldName;
    }

    public UUID getWorldId() {
        return worldId;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public String getWorldName() {
        return worldName;
    }

    public static ChunkInfo wrap(Chunk chunk) {
        return new ChunkInfo(chunk.getWorld().getUID(), chunk.getX(), chunk.getZ(), chunk.getWorld().getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(worldId, chunkX, chunkZ);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ChunkInfo)) {
            return false;
        }
        ChunkInfo o = (ChunkInfo) other;
        return o.worldId.equals(worldId) && o.chunkX == chunkX && o.chunkZ == chunkZ;
    }

}
