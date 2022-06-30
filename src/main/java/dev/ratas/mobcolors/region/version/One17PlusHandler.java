package dev.ratas.mobcolors.region.version;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;

import dev.ratas.slimedogcore.api.scheduler.SDCScheduler;
import dev.ratas.slimedogcore.api.wrappers.SDCWorldProvider;

public class One17PlusHandler implements Listener {
    private static final long COMPLETION_TIMEOUT_TICKS = 80L;
    private static final long CLEANUP_DELAY = 20L; // once a second
    private final SDCWorldProvider worldProvider;
    private final Map<ChunkInfo, ChunkCallbacks> chunksToCount = new HashMap<>();
    private CompletableFuture<Void> future = null;

    public One17PlusHandler(SDCWorldProvider worldProvider, SDCScheduler scheduler) {
        this.worldProvider = worldProvider;
        scheduler.runTaskTimer(this::cleanup, CLEANUP_DELAY, CLEANUP_DELAY);
    }

    public void addChunk(ChunkInfo chunk, Consumer<Entity> consumer, Runnable chunkCounter) {
        ChunkCallbacks callback;
        ChunkCallbacks prev = chunksToCount.put(chunk, callback = new ChunkCallbacks(consumer, chunkCounter));
        if (prev != null) {
            chunksToCount.put(chunk, prev);
            throw new IllegalStateException("Chunk " + chunk.getChunkX() + ", " + chunk.getChunkZ() + " in "
                    + chunk.getWorldName() + " already listed to be counted");
        }
        // count what is available now
        // whatever isn't available now, will be counted once the chunk li properly
        // loaded along all its entities
        countChunk(worldProvider.getWorldByName(chunk.getWorldName()).getChunkAt(chunk.getChunkX(), chunk.getChunkZ()),
                callback, false);
    }

    private void cleanup() {
        List<ChunkInfo> toFinalize = new ArrayList<>();
        for (Map.Entry<ChunkInfo, ChunkCallbacks> entry : chunksToCount.entrySet()) {
            if (entry.getValue().hasExpired()) {
                toFinalize.add(entry.getKey());
            }
        }
        for (ChunkInfo info : toFinalize) {
            ChunkCallbacks cb = chunksToCount.get(info);
            countChunk(worldProvider.getWorldByName(info.getWorldName()).getChunkAt(info.getChunkX(), info.getChunkZ()),
                    cb, false);
            chunksToCount.remove(info);
        }
    }

    public boolean hasPendingChunks() {
        return !chunksToCount.isEmpty();
    }

    public CompletableFuture<Void> reportWhenPendingChunksDone(SDCScheduler scheduler) {
        if (this.future != null) {
            throw new IllegalStateException("There is already a future");
        }
        this.future = new CompletableFuture<>();
        // timeout
        scheduler.runTaskLater(() -> {
            if (this.future != null && !this.future.isDone()) {
                Map<ChunkInfo, Integer> newEntitiesInChunks = getNrOfNewEntitiesInPendingChunks();
                chunksToCount.clear();
                // in my experience, if there's nothing new to load, then that's about it
                if (newEntitiesInChunks.isEmpty()) {
                    this.future.complete(null);
                } else {
                    Throwable exception = new PendingChunkTimeoutException(newEntitiesInChunks);
                    this.future.completeExceptionally(exception);
                }
                this.future = null;
            }
        }, COMPLETION_TIMEOUT_TICKS);
        return this.future;
    }

    // failsafe for when the EntitiesLoadEvent approach doesn't work for some reason
    private Map<ChunkInfo, Integer> getNrOfNewEntitiesInPendingChunks() {
        Map<ChunkInfo, Integer> map = new HashMap<>();
        for (Map.Entry<ChunkInfo, ChunkCallbacks> entry : chunksToCount.entrySet()) {
            ChunkInfo chunkInfo = entry.getKey();
            try {
                Chunk bukkitChunk = worldProvider.getWorldByName(chunkInfo.getWorldName())
                        .getChunkAt(chunkInfo.getChunkX(), chunkInfo.getChunkZ());
                ChunkCallbacks callback = entry.getValue();
                int before = callback.alreadyCounted.size();
                countChunk(bukkitChunk, callback, true);
                int diff = callback.alreadyCounted.size() - before;
                if (diff > 0) {
                    map.put(chunkInfo, diff);
                }
            } catch (Exception e) {
                // TODO - better exception handling
                e.printStackTrace();
            }
        }
        return map;
    }

    private boolean shouldReportPendingChunksDone() {
        return this.future != null;
    }

    private void reportPendingChunksDone() {
        this.future.complete(null);
        this.future = null;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityLoad(EntitiesLoadEvent event) {
        Chunk bukkitChunk = event.getChunk();
        ChunkInfo chunk = ChunkInfo.wrap(bukkitChunk);
        ChunkCallbacks callback = chunksToCount.remove(chunk);
        if (callback == null) {
            return;
        }
        countChunk(bukkitChunk, callback, true);
    }

    private void countChunk(Chunk bukkitChunk, ChunkCallbacks callback, boolean runChunkCounter) {
        if (runChunkCounter) {
            callback.chunkCounter.run(); // count chunk
        }
        for (Entity e : bukkitChunk.getEntities()) {
            callback.countEntity(e); // does not count double
        }
        if (!hasPendingChunks() && shouldReportPendingChunksDone()) { // is done
            reportPendingChunksDone();
        }
    }

    private class ChunkCallbacks {
        private final Consumer<Entity> consumer;
        private final Runnable chunkCounter;
        private final Set<Entity> alreadyCounted = new HashSet<>();
        private final long start = System.currentTimeMillis();

        private ChunkCallbacks(Consumer<Entity> consumer, Runnable chunkCounter) {
            this.consumer = consumer;
            this.chunkCounter = chunkCounter;
        }

        private void countEntity(Entity entity) {
            if (alreadyCounted.contains(entity)) {
                return;
            }
            consumer.accept(entity);
            alreadyCounted.add(entity);
        }

        private boolean hasExpired() {
            return System.currentTimeMillis() - start > COMPLETION_TIMEOUT_TICKS * 50;
        }

    }

    public class PendingChunkTimeoutException extends IllegalStateException {
        private final Map<ChunkInfo, Integer> chunksToCheck;

        private PendingChunkTimeoutException(Map<ChunkInfo, Integer> chunksToCheck) {
            super("Had chunks left to check after a timeout of " + COMPLETION_TIMEOUT_TICKS + " ticks.\n "
                    + "This is likely due to the server still starting up and some of the chunks "
                    + "listed might have fired the EntitiesLoadEvent at an inconvenient time."
                    + "There has been an attempt to load the entities within the chunks in question. "
                    + "Some entities were laoded in addition for some of these chunks, these are also given: "
                    + chunksToCheck);
            this.chunksToCheck = chunksToCheck;
        }

        public Map<ChunkInfo, Integer> getChunksToCheck() {
            return chunksToCheck;
        }

    }

}
