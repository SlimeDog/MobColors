package dev.ratas.mobcolors.scheduling;

import org.bukkit.Chunk;
import dev.ratas.mobcolors.region.RegionInfo;

public abstract class AbstractMultiChunkTask implements LongTask {
    private final RegionInfo regionInfo;
    private long chunksDone = 0;
    private int curX;
    private int curZ;
    private long ticks = 0;

    public AbstractMultiChunkTask(RegionInfo regionInfo) {
        this.regionInfo = regionInfo;
        // this.world = world;
        // this.minChunkX = minChunkX;
        // this.minChunkZ = minChunkZ;
        // this.maxChunkX = maxChunkX;
        // this.maxChunkZ = maxChunkZ;
        this.curX = regionInfo.getStartChunkX();
        this.curZ = regionInfo.getStartChunkZ();
        // this.totalChunks = (maxChunkX + 1 - minChunkX) * (maxChunkZ + 1 - minChunkZ);
        // this.ignoreUngenerated = ignoreUngenerated;
    }

    // public int getMinChunkX() {
    // return minChunkX;
    // }

    // public int getMaxChunkX() {
    // return maxChunkX;
    // }

    // public int getMinChunkZ() {
    // return minChunkZ;
    // }

    // public int getMaxChunkZ() {
    // return maxChunkZ;
    // }

    public Chunk getCurrentChunk() {
        return regionInfo.getWorld().getChunkAt(curX, curZ);
    }

    public boolean isCurrentChunkGenerated() {
        return regionInfo.getWorld().isChunkGenerated(curX, curZ);
    }

    private void advance() {
        advanceOnce(); // ungenerated ignoring will be handled in region info
    }

    private void advanceOnce() {
        curX = regionInfo.getChunkXFor(chunksDone);
        curZ = regionInfo.getChunkZFor(chunksDone);
    }

    @Override
    public void performAtomicPart() {
        // ungenerated ignoring will be handled in region info
        Chunk chunk = getCurrentChunk();
        processChunk(chunk);
        chunksDone++;
        if (!isDone()) {
            advance();
        }
    }

    @Override
    public long getNumberOfParts() {
        return regionInfo.getNumberOfTotalChunks();
    }

    @Override
    public long getNumberOfPartsDone() {
        return chunksDone;
    }

    @Override
    public boolean isDone() {
        return chunksDone >= regionInfo.getNumberOfTotalChunks();
    }

    @Override
    public void tickUpdate() {
        if ((++ticks % getTicksForUpdate()) == 0) {
            onUpdateTime();
        }
    }

    public abstract void processChunk(Chunk chunk);

}
