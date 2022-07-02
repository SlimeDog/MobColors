package dev.ratas.mobcolors.scheduling;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.ratas.mobcolors.region.DistanceRegionInfo;
import dev.ratas.mobcolors.region.RectangularRegionInfo;
import dev.ratas.mobcolors.region.RegionInfo;
import dev.ratas.mobcolors.region.version.mock.MockWorld;

public class SimpleTaskSchedulerTest {
    public static final UUID WORLD_UUID = UUID.randomUUID();
    public static final long MAX_MS = 50;
    public static final double UPDATE_PROGRESS = 0.01;
    public static final int REGION_X = 0;
    public static final int REGION_Z = 0;
    public static final double CHANCE_NOT_GEN = 0.1;
    private SimpleTaskScheduler scheduler;
    private int expectedTotalChunks;
    private AtomicInteger chunks;
    private AtomicInteger updates;
    private AtomicInteger completions;
    private AtomicInteger nonGenerated;
    private MockWorld world;

    @BeforeEach
    public void setup() {
        scheduler = new SimpleTaskScheduler(MAX_MS);
        chunks = new AtomicInteger(0);
        updates = new AtomicInteger(0);
        completions = new AtomicInteger(0);
        nonGenerated = new AtomicInteger(0);
        world = new PartiallyGeneratedMockWorld(CHANCE_NOT_GEN, () -> nonGenerated.incrementAndGet());
    }

    private void test_SchedulerWithInfo(RegionInfo regionInfo, boolean expectNonGenerations) {
        SimpleRegionTaskDelegator task = new MockSimpleRegionTaskDelegator(regionInfo, (a, b) -> {
            chunks.incrementAndGet();
        }, () -> {
            Assertions.assertEquals(1, completions.incrementAndGet());
            Assertions.assertEquals(expectedTotalChunks, chunks.get() + nonGenerated.get());
            if (expectNonGenerations) {
                Assertions.assertTrue(nonGenerated.get() > 0);
            }
        }, (a, b) -> {
            updates.incrementAndGet();
        });
        scheduler.scheduleTask(task);
        Assertions.assertTrue(scheduler.hasRunningTask());
        while (scheduler.hasRunningTask()) {
            scheduler.run();
        }
        Assertions.assertEquals(1, completions.get());
    }

    @Test
    public void test_AbstractMultiChunkTask_Region_does_all_generated_chunks() {
        RegionInfo regionInfo = new RectangularRegionInfo(world, REGION_X, REGION_Z, true);
        expectedTotalChunks = 32 * 32;
        test_SchedulerWithInfo(regionInfo, true);
    }

    // TODO - with DISTANCE
    // todo

    @Test
    public void test_AbstractMultiChunkTask_does_all_chunks_within_distance10() {
        Location center = new Location(world, 0, 0, 0);
        double distance = 10;
        RegionInfo regionInfo = new DistanceRegionInfo(center, distance, true);
        expectedTotalChunks = 4;
        test_SchedulerWithInfo(regionInfo, false);
    }

    @Test
    public void test_AbstractMultiChunkTask_does_all_chunks_within_distance5_midchunk() {
        Location center = new Location(world, 7, 63, 7);
        double distance = 5;
        RegionInfo regionInfo = new DistanceRegionInfo(center, distance, true);
        expectedTotalChunks = 1;
        test_SchedulerWithInfo(regionInfo, false);
    }

    public static class MockSimpleRegionTaskDelegator extends SimpleRegionTaskDelegator {

        public MockSimpleRegionTaskDelegator(RegionInfo info, BiConsumer<Chunk, Boolean> chunkConsumer,
                Runnable onCompletion, BiConsumer<Long, Long> onUpdate) {
            super(info, chunkConsumer, onCompletion, UPDATE_PROGRESS, onUpdate, true);
        }

    }

    public static class PartiallyGeneratedMockWorld extends MockWorld {
        private final double chance4NotGen;
        private final Runnable onNotGenerated;

        public PartiallyGeneratedMockWorld(double chance4NotGen, Runnable onNotGenerated) {
            super(WORLD_UUID, "world-with-name");
            this.chance4NotGen = chance4NotGen;
            this.onNotGenerated = onNotGenerated;
        }

        @Override
        public boolean isChunkGenerated(int x, int z) {
            if (ThreadLocalRandom.current().nextDouble() > chance4NotGen) {
                return true;
            } else {
                onNotGenerated.run();
                return false;
            }
        }

    }

}
