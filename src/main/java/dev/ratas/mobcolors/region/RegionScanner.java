package dev.ratas.mobcolors.region;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;

import dev.ratas.mobcolors.config.mob.MobTypes;
import dev.ratas.mobcolors.events.ListenerRegistrator;
import dev.ratas.mobcolors.region.version.ChunkInfo;
import dev.ratas.mobcolors.region.version.One17PlusHandler;
import dev.ratas.mobcolors.region.version.Version;
import dev.ratas.mobcolors.scheduling.SimpleRegionTaskDelegator;
import dev.ratas.mobcolors.scheduling.TaskScheduler;
import dev.ratas.mobcolors.scheduling.abstraction.Scheduler;
import dev.ratas.mobcolors.utils.LogUtils;
import dev.ratas.mobcolors.utils.WorldProvider;

public class RegionScanner extends AbstractRegionHandler {
    private final Scheduler bukkitScheduler;
    private final TaskScheduler scheduler;
    private final One17PlusHandler eventLoadHandler;

    public RegionScanner(Scheduler bukkitScheduler, TaskScheduler scheduler, ListenerRegistrator registrator,
            WorldProvider worldProvider) {
        this.bukkitScheduler = bukkitScheduler;
        this.scheduler = scheduler;
        if (Version.hasEntitiesLoadEvent()) {
            eventLoadHandler = new One17PlusHandler(worldProvider);
            registrator.register(eventLoadHandler);
        } else {
            eventLoadHandler = null;
        }
    }

    public boolean isBusy() {
        return scheduler.hasRunningTask();
    }

    public CompletableFuture<ScanReport<?>> scanRegion(RegionInfo info, RegionOptions options, double updateProgress,
            BiConsumer<Long, Long> updaterConsumer) {
        CompletableFuture<ScanReport<?>> future = new CompletableFuture<>();
        ScanReport<?> report;
        if (!options.hasTargetType()) {
            report = new MultiReport();
        } else {
            report = new ScanReport<>(options.getTargetType(), MobTypes.getFunctionForType(options.getTargetType()));
        }
        scheduler.scheduleTask(new SimpleRegionTaskDelegator(info,
                (chunk, wasLoaded) -> checkChunk(info, chunk, report, options, wasLoaded), () -> {
                    if (eventLoadHandler == null || !eventLoadHandler.hasPendingChunks()) {
                        future.complete(report);
                    } else {
                        eventLoadHandler.reportWhenPendingChunksDone(bukkitScheduler).whenComplete((v, e) -> {
                            if (e != null) {
                                LogUtils.getLogger().warning(e.getMessage());
                            }
                            future.complete(report);
                        });
                    }
                }, updateProgress, updaterConsumer));
        return future;
    }

    private void checkChunk(RegionInfo info, Chunk chunk, ScanReport<?> report, RegionOptions options,
            boolean wasLoaded) {
        if (eventLoadHandler != null && !wasLoaded) {
            eventLoadHandler.addChunk(ChunkInfo.wrap(chunk), (entity) -> dealWithEntity(entity, options, info, report),
                    () -> report.countAChunk());
            chunk.load();
            return;
        } else {
            report.countAChunk();
            for (Entity entity : chunk.getEntities()) {
                dealWithEntity(entity, options, info, report);
            }
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
