package dev.ratas.mobcolors.region;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sheep;

import dev.ratas.mobcolors.scheduling.SimpleRegionTaskDelegator;
import dev.ratas.mobcolors.scheduling.TaskScheduler;
import dev.ratas.mobcolors.utils.PetUtils;

public class RegionScanner {
    private final TaskScheduler scheduler;

    public RegionScanner(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public CompletableFuture<ScanReport> scanRegion(World world, int regionX, int regionZ, boolean doLeashed,
            boolean doPets, long updateTicks, BiConsumer<Long, Long> updaterConsumer, boolean ignoreUngenerated,
            EntityType targetType) {
        CompletableFuture<ScanReport> future = new CompletableFuture<>();
        ScanReport report = new ScanReport();
        scheduler.scheduleTask(new SimpleRegionTaskDelegator(world, regionX, regionZ,
                (chunk) -> checkChunk(chunk, report, !doLeashed, !doPets, targetType), () -> future.complete(report),
                updateTicks, updaterConsumer, ignoreUngenerated));
        return future;
    }

    private void checkChunk(Chunk chunk, ScanReport report, boolean skipLeashed, boolean skipPets,
            EntityType targetType) {
        report.countAChunk();
        for (Entity entity : chunk.getEntities()) {
            if (targetType == null || entity.getType().equals(targetType)) {
                if (entity instanceof LivingEntity) {
                    if (skipLeashed && ((LivingEntity) entity).isLeashed()) {
                        continue; // skip
                    }
                }
                if (skipPets && PetUtils.isPet(entity)) {
                    continue;
                }
                report.count((Sheep) entity);
            }
        }
    }

}
