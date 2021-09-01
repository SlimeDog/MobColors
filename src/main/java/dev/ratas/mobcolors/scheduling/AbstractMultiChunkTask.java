package dev.ratas.mobcolors.scheduling;

import org.bukkit.Chunk;
import dev.ratas.mobcolors.region.RegionInfo;

public abstract class AbstractMultiChunkTask implements LongTask {
    private final RegionInfo regionInfo;
    private long chunksDone = 0;
    private int curX;
    private int curZ;
    private double lastUpdateProgress = 0;

    public AbstractMultiChunkTask(RegionInfo regionInfo) {
        this.regionInfo = regionInfo;
        this.curX = regionInfo.getStartChunkX();
        this.curZ = regionInfo.getStartChunkZ();
    }

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
        double nextProgress = lastUpdateProgress + getUpdateProgress();
        double currentProgress = (double) getNumberOfPartsDone() / getNumberOfParts();
        if (currentProgress >= nextProgress) {
            onUpdateTime();
            while (lastUpdateProgress < currentProgress) { // in case we went from 0 to 51% with a 25% step,
                                                           // don't update next tick but at 75%
                lastUpdateProgress += getUpdateProgress();
            }
        }
    }

    public abstract void processChunk(Chunk chunk);

}
