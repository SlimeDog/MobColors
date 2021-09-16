package dev.ratas.mobcolors.region.version.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;

import dev.ratas.mobcolors.region.version.ChunkInfo;

public class MockChunk extends ChunkInfo implements Chunk {
    private final List<Entity> entities = new ArrayList<>();
    private final MockWorld world;

    public MockChunk(MockWorld world, int chunkX, int chunkZ) {
        super(world.getUID(), chunkX, chunkZ, world.getName());
        this.world = world;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getX() {
        return getChunkX();
    }

    @Override
    public int getZ() {
        return getChunkZ();
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public ChunkSnapshot getChunkSnapshot() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public ChunkSnapshot getChunkSnapshot(boolean includeMaxblocky, boolean includeBiome,
            boolean includeBiomeTempRain) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isEntitiesLoaded() {
        return true;
    }

    @Override
    public Entity[] getEntities() {
        return entities.toArray(new Entity[0]);
    }

    @Override
    public BlockState[] getTileEntities() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isLoaded() {
        return true;
    }

    @Override
    public boolean load(boolean generate) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean load() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean unload(boolean save) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean unload() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isSlimeChunk() {
        return false;
    }

    @Override
    public boolean isForceLoaded() {
        return false;
    }

    @Override
    public void setForceLoaded(boolean forced) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean addPluginChunkTicket(Plugin plugin) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean removePluginChunkTicket(Plugin plugin) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Collection<Plugin> getPluginChunkTickets() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public long getInhabitedTime() {
        return 0;
    }

    @Override
    public void setInhabitedTime(long ticks) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean contains(BlockData block) {
        throw new IllegalStateException("Not implemented yet");
    }

}
