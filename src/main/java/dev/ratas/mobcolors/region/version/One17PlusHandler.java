package dev.ratas.mobcolors.region.version;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;

public class One17PlusHandler implements Listener {
    private final Map<ChunkInfo, ChunkCallbacks> chunksToCount = new HashMap<>();

    public void addChunk(ChunkInfo chunk, Consumer<Entity> consumer, Runnable chunkCounter) {
        ChunkCallbacks prev = chunksToCount.put(chunk, new ChunkCallbacks(consumer, chunkCounter));
        if (prev != null) {
            chunksToCount.put(chunk, prev);
            throw new IllegalStateException("Chunk " + chunk.getChunkX() + ", " + chunk.getChunkZ() + " in "
                    + chunk.getWorldName() + " already listed to be counted");
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityLoad(EntitiesLoadEvent event) {
        Chunk bukkitChunk = event.getChunk();
        ChunkInfo chunk = ChunkInfo.wrap(bukkitChunk);
        ChunkCallbacks callback = chunksToCount.remove(chunk);
        if (callback == null) {
            return;
        }
        callback.chunkCounter.run(); // count chunk
        for (Entity e : bukkitChunk.getEntities()) {
            callback.consumer.accept(e);
        }
    }

    private class ChunkCallbacks {
        private final Consumer<Entity> consumer;
        private final Runnable chunkCounter;

        private ChunkCallbacks(Consumer<Entity> consumer, Runnable chunkCounter) {
            this.consumer = consumer;
            this.chunkCounter = chunkCounter;
        }
    }

}
