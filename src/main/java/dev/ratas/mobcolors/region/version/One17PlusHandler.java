package dev.ratas.mobcolors.region.version;

import java.util.HashMap;
import java.util.HashSet;
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

import dev.ratas.mobcolors.scheduling.abstraction.Scheduler;
import dev.ratas.mobcolors.utils.WorldProvider;

public class One17PlusHandler implements Listener {
    private static final long COMPLETION_TIMEOUT_TICKS = 80L;
    private final WorldProvider worldProvider;
    private final Map<ChunkInfo, ChunkCallbacks> chunksToCount = new HashMap<>();
    private CompletableFuture<Void> future = null;

    public One17PlusHandler(WorldProvider worldProvider) {
        this.worldProvider = worldProvider;
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
        countChunk(worldProvider.getWorld(chunk.getWorldName()).getChunkAt(chunk.getChunkX(), chunk.getChunkZ()),
                callback, false);
    }

    public boolean hasPendingChunks() {
        return !chunksToCount.isEmpty();
    }

    public CompletableFuture<Void> reportWhenPendingChunksDone(Scheduler scheduler) {
        if (this.future != null) {
            throw new IllegalStateException("There is already a future");
        }
        this.future = new CompletableFuture<>();
        // timeout
        scheduler.schedule(() -> {
            if (this.future != null && !this.future.isDone()) {
                Throwable exception = new PendingChunkTimeoutException(new HashSet<>(chunksToCount.keySet()));
                countPendingChunks();
                chunksToCount.clear();
                this.future.completeExceptionally(exception);
                this.future = null;
            }
        }, COMPLETION_TIMEOUT_TICKS);
        return this.future;
    }

    // failsafe for when the EntitiesLoadEvent approach doesn't work for some reason
    private void countPendingChunks() {
        for (Map.Entry<ChunkInfo, ChunkCallbacks> entry : chunksToCount.entrySet()) {
            ChunkInfo chunkInfo = entry.getKey();
            try {
                Chunk bukkitChunk = worldProvider.getWorld(chunkInfo.getWorldName()).getChunkAt(chunkInfo.getChunkX(),
                        chunkInfo.getChunkZ());
                countChunk(bukkitChunk, entry.getValue(), true);
            } catch (Exception e) {
                // TODO - better exception handling
                e.printStackTrace();
            }
        }
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
    }

    public class PendingChunkTimeoutException extends IllegalStateException {
        private final Set<ChunkInfo> chunksToCheck;

        private PendingChunkTimeoutException(Set<ChunkInfo> chunksToCheck) {
            super("Had chunks left to check after a timeout of " + COMPLETION_TIMEOUT_TICKS + " ticks.\n "
                    + "This is likely due to the server still starting up and some of the chunks "
                    + "listed might have fired the EntitiesLoadEvent at an inconvenient time."
                    + "There has been an attempt to load the entities within the chunks in question:" + chunksToCheck);
            this.chunksToCheck = chunksToCheck;
        }

        public Set<ChunkInfo> getChunksToCheck() {
            return chunksToCheck;
        }

    }

}
