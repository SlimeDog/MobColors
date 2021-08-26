package dev.ratas.mobcolors.region;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import dev.ratas.mobcolors.config.mob.MobTypes;
import dev.ratas.mobcolors.scheduling.SimpleRegionTaskDelegator;
import dev.ratas.mobcolors.scheduling.TaskScheduler;
import dev.ratas.mobcolors.utils.PetUtils;

public class RegionScanner {
    private final TaskScheduler scheduler;

    public RegionScanner(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public CompletableFuture<ScanReport<?>> scanRegion(RegionInfo info, boolean doLeashed, boolean doPets,
            long updateTicks, BiConsumer<Long, Long> updaterConsumer, EntityType targetType) {
        CompletableFuture<ScanReport<?>> future = new CompletableFuture<>();
        ScanReport<?> report;
        if (targetType == null) {
            report = new MultiReport();
        } else {
            report = new ScanReport<>(targetType, MobTypes.getFunctionForType(targetType));
        }
        scheduler.scheduleTask(new SimpleRegionTaskDelegator(info,
                (chunk) -> checkChunk(info, chunk, report, !doLeashed, !doPets, targetType),
                () -> future.complete(report), updateTicks, updaterConsumer));
        return future;
    }

    private void checkChunk(RegionInfo info, Chunk chunk, ScanReport<?> report, boolean skipLeashed, boolean skipPets,
            EntityType targetType) {
        report.countAChunk();
        for (Entity entity : chunk.getEntities()) {
            if (targetType == null || entity.getType().equals(targetType)) {
                Class<?> clazz = MobTypes.getInterestingClass(entity);
                if (clazz == null) {
                    continue; // ignore - not of correct type
                }
                if (entity instanceof LivingEntity) {
                    if (skipLeashed && ((LivingEntity) entity).isLeashed()) {
                        continue; // skip
                    }
                }
                if (skipPets && PetUtils.isPet(entity)) {
                    continue;
                }
                if (!info.isInRange(entity)) {
                    continue;
                }
                report.count(entity);
            }
        }
    }

}
