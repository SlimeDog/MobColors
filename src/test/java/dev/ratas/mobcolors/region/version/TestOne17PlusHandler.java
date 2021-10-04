package dev.ratas.mobcolors.region.version;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.ratas.mobcolors.region.version.mock.MockChunk;
import dev.ratas.mobcolors.region.version.mock.MockEntity;
import dev.ratas.mobcolors.region.version.mock.MockScheduler;
import dev.ratas.mobcolors.region.version.mock.MockWorld;
import dev.ratas.mobcolors.region.version.mock.MockWorldProvider;

public class TestOne17PlusHandler {
    private MockWorldProvider wp;
    private MockWorld mw;
    private One17PlusHandler handler;
    private int counter;

    @BeforeEach
    public void setup() {
        wp = new MockWorldProvider(mw = new MockWorld(UUID.randomUUID(), "worldName#132"));
        handler = new One17PlusHandler(wp, new MockScheduler());
        counter = 0;
    }

    @Test
    public void test_HandlerDoesNotFindUnknownChunk() {
        int chunkX = -1;
        int chunkZ = -1;
        MockChunk chunk = new MockChunk(mw, chunkX, chunkZ);
        mw.setChunk(chunk);
        Assertions.assertAll("Add Chunk method was exceptional",
                () -> handler.addChunk(new ChunkInfo(UUID.randomUUID(), chunkX, chunkZ), e -> {
                }, () -> {
                }));
    }

    @Test
    public void test_HandlerCanHandleUnregisteredChunkEvent() {
        MockChunk chunk = new MockChunk(mw, -1, 1);
        mw.setChunk(chunk);
        EntitiesLoadEvent event = new EntitiesLoadEvent(chunk, Arrays.asList(chunk.getEntities()));
        handler.onEntityLoad(event);
    }

    @Test
    public void test_HandlerCallsRunnableOnEvent() {
        MockChunk chunk = new MockChunk(mw, -1, 1);
        mw.setChunk(chunk);
        chunk.addEntity(
                new MockEntity(UUID.randomUUID(), "mobName", new Location(mw, -1, 25, 4), 24, EntityType.SHEEP));
        handler.addChunk(chunk, e -> {
            this.counter++;
        }, () -> {
        });
        EntitiesLoadEvent event = new EntitiesLoadEvent(chunk, Arrays.asList(chunk.getEntities()));
        handler.onEntityLoad(event);
        Assertions.assertFalse(this.counter == 0, "Handler did not call event!");
        Assertions.assertFalse(this.counter > 1, "Handler did call tue event too many times: " + this.counter);
    }

    @Test
    public void test_HandlerDoesntCallRunnableOnSecondEvent() {
        MockChunk chunk = new MockChunk(mw, -1, 1);
        mw.setChunk(chunk);
        chunk.addEntity(
                new MockEntity(UUID.randomUUID(), "mobName", new Location(mw, -1, 25, 4), 24, EntityType.SHEEP));
        handler.addChunk(chunk, e -> {
            this.counter++;
        }, () -> {
        });
        EntitiesLoadEvent event = new EntitiesLoadEvent(chunk, Arrays.asList(chunk.getEntities()));
        handler.onEntityLoad(event);
        handler.onEntityLoad(event);
        Assertions.assertFalse(this.counter == 0, "Handler did not call event!");
        Assertions.assertFalse(this.counter > 1, "Handler did call tue event too many times: " + this.counter);
    }

    @Test
    public void test_HandlerCallsRunnableForEachOnEvent() {
        MockChunk chunk = new MockChunk(mw, -1, 1);
        mw.setChunk(chunk);
        chunk.addEntity(new MockEntity(UUID.randomUUID(), "mobName", new Location(mw, 1, 25, -4), 24, EntityType.FOX));
        chunk.addEntity(
                new MockEntity(UUID.randomUUID(), "mobName2", new Location(mw, 11, 5, 4), 24, EntityType.AXOLOTL));
        chunk.addEntity(
                new MockEntity(UUID.randomUUID(), "mobName3", new Location(mw, -11, 252, -4), 24, EntityType.SHULKER));
        handler.addChunk(chunk, e -> {
            this.counter++;
        }, () -> {
        });
        EntitiesLoadEvent event = new EntitiesLoadEvent(chunk, Arrays.asList(chunk.getEntities()));
        handler.onEntityLoad(event);
        Assertions.assertFalse(this.counter < 3, "Handler did not call event enough: " + this.counter);
        Assertions.assertFalse(this.counter > 3, "Handler did call tue event too many times: " + this.counter);
    }

}
