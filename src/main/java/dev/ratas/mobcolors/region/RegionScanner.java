package dev.ratas.mobcolors.region;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;

import dev.ratas.mobcolors.config.mob.MobTypes;
import dev.ratas.mobcolors.scheduling.SimpleRegionTaskDelegator;
import dev.ratas.mobcolors.scheduling.TaskScheduler;

public class RegionScanner extends AbstractRegionHandler {
    private final TaskScheduler scheduler;

    public RegionScanner(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public CompletableFuture<ScanReport<?>> scanRegion(RegionInfo info, RegionOptions options, long updateTicks,
            BiConsumer<Long, Long> updaterConsumer) {
        CompletableFuture<ScanReport<?>> future = new CompletableFuture<>();
        ScanReport<?> report;
        if (!options.hasTargetType()) {
            report = new MultiReport();
        } else {
            report = new ScanReport<>(options.getTargetType(), MobTypes.getFunctionForType(options.getTargetType()));
        }
        scheduler.scheduleTask(new SimpleRegionTaskDelegator(info, (chunk) -> checkChunk(info, chunk, report, options),
                () -> future.complete(report), updateTicks, updaterConsumer));
        return future;
    }

    private void checkChunk(RegionInfo info, Chunk chunk, ScanReport<?> report, RegionOptions options) {
        report.countAChunk();
        for (Entity entity : chunk.getEntities()) {
            dealWithEntity(entity, options, info, report);
        }
    }

    private void dealWithEntity(Entity entity, RegionOptions options, RegionInfo info, ScanReport<?> report) {
        if (isApplicable(entity, options, info)) {
            countApplicableEntity(entity, report);
        }
    }

    void countApplicableEntity(Entity entity, ScanReport<?> report) {
        report.count(entity);
    }

}
