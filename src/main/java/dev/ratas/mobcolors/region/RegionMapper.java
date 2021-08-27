package dev.ratas.mobcolors.region;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
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
            long updateTicks, BiConsumer<Long, Long> updaterConsumer, EntityType targetType) {
        CompletableFuture<ScanReport<?>> future = new CompletableFuture<>();
        ScanReport<?> report = new MultiReport();
        scheduler.scheduleTask(new SimpleRegionTaskDelegator(info,
                (chunk) -> dyeMobInChunk(info, chunk, report, !doLeashed, !doPets, targetType),
                () -> future.complete(report), updateTicks, updaterConsumer));
        return future;
    }

    private void dyeMobInChunk(RegionInfo info, Chunk chunk, ScanReport<?> report, boolean skipLeashed,
            boolean skipPets, EntityType targetType) {
        report.countAChunk();
        for (Entity entity : chunk.getEntities()) {
            if (targetType != null && !entity.getType().equals(targetType)) {
                continue;
            }
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
            if (spawnListener.handleEntity((LivingEntity) entity, SpawnReason.CUSTOM)) {
                report.count(entity);
            }
        }
    }

}
