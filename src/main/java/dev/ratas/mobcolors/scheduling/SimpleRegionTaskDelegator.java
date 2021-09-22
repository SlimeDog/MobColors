package dev.ratas.mobcolors.scheduling;

import java.util.function.BiConsumer;

import org.bukkit.Chunk;

import dev.ratas.mobcolors.region.RegionInfo;

public class SimpleRegionTaskDelegator extends AbstractMultiChunkTask {
    private final BiConsumer<Chunk, Boolean> chunkConsumer;
    private final Runnable onCompletion;
    private final double updatePgoress;
    private final BiConsumer<Long, Long> onUpdate;

    public SimpleRegionTaskDelegator(RegionInfo info, BiConsumer<Chunk, Boolean> chunkConsumer, Runnable onCompletion,
            double updateProgress, BiConsumer<Long, Long> onUpdate) {
        this(info, chunkConsumer, onCompletion, updateProgress, onUpdate, false);
    }

    public SimpleRegionTaskDelegator(RegionInfo info, BiConsumer<Chunk, Boolean> chunkConsumer, Runnable onCompletion,
            double updateProgress, BiConsumer<Long, Long> onUpdate, boolean ignoreUngenerated) {
        super(info);
        this.chunkConsumer = chunkConsumer;
        this.onCompletion = onCompletion;
        this.updatePgoress = updateProgress;
        this.onUpdate = onUpdate;
    }

    @Override
    public void processChunk(Chunk chunk, boolean wasLoadedPreviously) {
        try {
            chunkConsumer.accept(chunk, wasLoadedPreviously);
        } catch (Exception e) {
            e.printStackTrace(); // TODO - proper error handling
        }
    }

    @Override
    public void onComplete() {
        onCompletion.run();
    }

    @Override
    public double getUpdateProgress() {
        return updatePgoress;
    }

    @Override
    public void onUpdateTime() {
        if (onUpdate != null) {
            onUpdate.accept(getNumberOfPartsDone(), getNumberOfParts());
        }
    }

}
