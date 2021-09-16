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
    private final Map<ChunkInfo, Consumer<Entity>> chunksToCount = new HashMap<>();

    public void addChunk(ChunkInfo chunk, Consumer<Entity> consumer) {
        Consumer<Entity> prev = chunksToCount.put(chunk, consumer);
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
        Consumer<Entity> consumer = chunksToCount.remove(chunk);
        if (consumer == null) {
            return;
        }
        for (Entity e : bukkitChunk.getEntities()) {
            consumer.accept(e);
        }
    }

}
