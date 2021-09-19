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
import dev.ratas.mobcolors.region.version.mock.MockWorld;

public class TestOne17PlusHandler {
    private One17PlusHandler handler;
    private int counter;

    @BeforeEach
    public void setup() {
        handler = new One17PlusHandler();
        counter = 0;
    }

    @Test
    public void test_HandlerDoesNotFindUnknownChunk() {
        Assertions.assertAll("Add Chunk method was exceptional",
                () -> handler.addChunk(new ChunkInfo(UUID.randomUUID(), -1, 1), e -> {
                }, () -> {
                }));
    }

    @Test
    public void test_HandlerCanHandleUnregisteredChunkEvent() {
        MockWorld world = new MockWorld(UUID.randomUUID(), "worldName#132");
        MockChunk chunk = new MockChunk(world, -1, 1);
        EntitiesLoadEvent event = new EntitiesLoadEvent(chunk, Arrays.asList(chunk.getEntities()));
        handler.onEntityLoad(event);
    }

    @Test
    public void test_HandlerCallsRunnableOnEvent() {
        MockWorld world = new MockWorld(UUID.randomUUID(), "worldName#132");
        MockChunk chunk = new MockChunk(world, -1, 1);
        chunk.addEntity(
                new MockEntity(UUID.randomUUID(), "mobName", new Location(world, -1, 25, 4), 24, EntityType.SHEEP));
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
        MockWorld world = new MockWorld(UUID.randomUUID(), "worldName#132");
        MockChunk chunk = new MockChunk(world, -1, 1);
        chunk.addEntity(
                new MockEntity(UUID.randomUUID(), "mobName", new Location(world, -1, 25, 4), 24, EntityType.SHEEP));
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
        MockWorld world = new MockWorld(UUID.randomUUID(), "worldName#132");
        MockChunk chunk = new MockChunk(world, -1, 1);
        chunk.addEntity(
                new MockEntity(UUID.randomUUID(), "mobName", new Location(world, 1, 25, -4), 24, EntityType.FOX));
        chunk.addEntity(
                new MockEntity(UUID.randomUUID(), "mobName2", new Location(world, 11, 5, 4), 24, EntityType.AXOLOTL));
        chunk.addEntity(new MockEntity(UUID.randomUUID(), "mobName3", new Location(world, -11, 252, -4), 24,
                EntityType.SHULKER));
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
