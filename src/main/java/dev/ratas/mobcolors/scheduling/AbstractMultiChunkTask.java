package dev.ratas.mobcolors.scheduling;

import org.bukkit.Chunk;
import org.bukkit.World;

public abstract class AbstractMultiChunkTask implements LongTask {
    private final World world;
    private final int minChunkX;
    private final int minChunkZ;
    private final int maxChunkX;
    private final int maxChunkZ;
    private final long totalChunks;
    private long chunksDone = 0;
    private int curX;
    private int curZ;
    private final boolean ignoreUngenerated;
    private long ticks = 0;

    public AbstractMultiChunkTask(World world, int minChunkX, int minChunkZ, int maxChunkX, int maxChunkZ,
            boolean ignoreUngenerated) {
        this.world = world;
        this.minChunkX = minChunkX;
        this.minChunkZ = minChunkZ;
        this.maxChunkX = maxChunkX;
        this.maxChunkZ = maxChunkZ;
        this.curX = minChunkX;
        this.curZ = minChunkZ;
        this.totalChunks = (maxChunkX + 1 - minChunkX) * (maxChunkZ + 1 - minChunkZ);
        this.ignoreUngenerated = ignoreUngenerated;
    }

    public int getMinChunkX() {
        return minChunkX;
    }

    public int getMaxChunkX() {
        return maxChunkX;
    }

    public int getMinChunkZ() {
        return minChunkZ;
    }

    public int getMaxChunkZ() {
        return maxChunkZ;
    }

    public Chunk getCurrentChunk() {
        return world.getChunkAt(curX, curZ);
    }

    public boolean isCurrentChunkGenerated() {
        return world.isChunkGenerated(curX, curZ);
    }

    private void advance() {
        chunksDone++;
        if (ignoreUngenerated) {
            advanceOnce();
            while (!isCurrentChunkGenerated() && !isDone()) {
                advanceOnce();
            }
        } else {
            advanceOnce();
        }
    }

    private void advanceOnce() {
        if (curX >= maxChunkX) {
            curX = minChunkX;
            curZ++;
        } else {
            curX++;
        }
    }

    @Override
    public void performAtomicPart() {
        if (ignoreUngenerated && !isCurrentChunkGenerated()) {
            advance();
        }
        if (ignoreUngenerated && !isCurrentChunkGenerated()) {
            return;
        }
        Chunk chunk = getCurrentChunk();
        processChunk(chunk);
        advance();
    }

    @Override
    public long getNumberOfParts() {
        return totalChunks;
    }

    @Override
    public long getNumberOfPartsDone() {
        return chunksDone;
    }

    @Override
    public boolean isDone() {
        return curZ > maxChunkZ;
    }

    @Override
    public void tickUpdate() {
        if ((++ticks % getTicksForUpdate()) == 0) {
            onUpdateTime();
        }
    }

    public abstract void processChunk(Chunk chunk);

}
