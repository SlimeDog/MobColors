package dev.ratas.mobcolors.scheduling;

import java.util.function.BiConsumer;

import org.bukkit.Chunk;
import org.bukkit.util.Consumer;

import dev.ratas.mobcolors.region.RegionInfo;

public class SimpleRegionTaskDelegator extends AbstractMultiChunkTask {
    private final Consumer<Chunk> chunkConsumer;
    private final Runnable onCompletion;
    private final double updatePgoress;
    private final BiConsumer<Long, Long> onUpdate;

    public SimpleRegionTaskDelegator(RegionInfo info, Consumer<Chunk> chunkConsumer, Runnable onCompletion,
            double updateProgress, BiConsumer<Long, Long> onUpdate) {
        this(info, chunkConsumer, onCompletion, updateProgress, onUpdate, false);
    }

    public SimpleRegionTaskDelegator(RegionInfo info, Consumer<Chunk> chunkConsumer, Runnable onCompletion,
            double updateProgress, BiConsumer<Long, Long> onUpdate, boolean ignoreUngenerated) {
        super(info);
        this.chunkConsumer = chunkConsumer;
        this.onCompletion = onCompletion;
        this.updatePgoress = updateProgress;
        this.onUpdate = onUpdate;
    }

    @Override
    public void processChunk(Chunk chunk) {
        try {
            chunkConsumer.accept(chunk);
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
