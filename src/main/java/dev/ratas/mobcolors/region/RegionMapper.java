package dev.ratas.mobcolors.region;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import dev.ratas.mobcolors.SpawnListener;
import dev.ratas.mobcolors.config.mob.MobTypes;
import dev.ratas.mobcolors.scheduling.SimpleRegionTaskDelegator;
import dev.ratas.mobcolors.scheduling.TaskScheduler;

public class RegionMapper {
    private final TaskScheduler scheduler;
    private final SpawnListener spawnListener;

    public RegionMapper(TaskScheduler scheduler, SpawnListener spawnListener) {
        this.scheduler = scheduler;
        this.spawnListener = spawnListener;
    }

    public CompletableFuture<ScanReport<?>> dyeEntitiesInRegion(RegionInfo info, boolean doLeashed, boolean doPets,
            long updateTicks, BiConsumer<Long, Long> updaterConsumer) {
        CompletableFuture<ScanReport<?>> future = new CompletableFuture<>();
        ScanReport<?> report = new MultiReport();
        scheduler.scheduleTask(new SimpleRegionTaskDelegator(info,
                (chunk) -> dyeSheepInChunk(info, chunk, report, !doLeashed, !doPets), () -> future.complete(report),
                updateTicks, updaterConsumer));
        return future;
    }

    private void dyeSheepInChunk(RegionInfo info, Chunk chunk, ScanReport<?> report, boolean skipLeashed,
            boolean skipPets) {
        report.countAChunk();
        for (Entity entity : chunk.getEntities()) {
            Class<?> clazz = MobTypes.getInterestingClass(entity);
            if (clazz == null) {
                continue; // ignore - not of correct type
            }
            if (!(entity instanceof LivingEntity)) {
                continue; // ignore
            }
            if (!info.isInRange(entity)) {
                continue;
            }
            spawnListener.handleEntity((LivingEntity) entity, SpawnReason.CUSTOM);
            report.count(entity);
        }
    }

}
