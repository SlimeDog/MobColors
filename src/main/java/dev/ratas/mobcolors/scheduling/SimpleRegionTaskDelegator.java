package dev.ratas.mobcolors.scheduling;

import java.util.function.BiConsumer;

import org.bukkit.Chunk;
import org.bukkit.util.Consumer;

import dev.ratas.mobcolors.region.RegionInfo;

public class SimpleRegionTaskDelegator extends AbstractMultiChunkTask {
    private final Consumer<Chunk> chunkConsumer;
    private final Runnable onCompletion;
    private final long updateTicks;
    private final BiConsumer<Long, Long> onUpdate;

    public SimpleRegionTaskDelegator(RegionInfo info, Consumer<Chunk> chunkConsumer, Runnable onCompletion,
            long updateTicks, BiConsumer<Long, Long> onUpdate) {
        this(info, chunkConsumer, onCompletion, updateTicks, onUpdate, false);
    }

    public SimpleRegionTaskDelegator(RegionInfo info, Consumer<Chunk> chunkConsumer, Runnable onCompletion,
            long updateTicks, BiConsumer<Long, Long> onUpdate, boolean ignoreUngenerated) {
        super(info);
        this.chunkConsumer = chunkConsumer;
        this.onCompletion = onCompletion;
        this.updateTicks = updateTicks;
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
    public long getTicksForUpdate() {
        return updateTicks;
    }

    @Override
    public void onUpdateTime() {
        if (onUpdate != null) {
            onUpdate.accept(getNumberOfPartsDone(), getNumberOfParts());
        }
    }

}
