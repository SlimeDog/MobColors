package dev.ratas.mobcolors.region;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import dev.ratas.mobcolors.SpawnListener;
import dev.ratas.mobcolors.events.ListenerRegistrator;
import dev.ratas.mobcolors.region.version.ChunkInfo;
import dev.ratas.mobcolors.region.version.One17PlusHandler;
import dev.ratas.mobcolors.region.version.Version;
import dev.ratas.mobcolors.scheduling.SimpleRegionTaskDelegator;
import dev.ratas.mobcolors.scheduling.TaskScheduler;

public class RegionMapper extends AbstractRegionHandler {
    private final TaskScheduler scheduler;
    private final SpawnListener spawnListener;
    private final RegionScanner scanner;
    private final One17PlusHandler eventLoadHandler;

    public RegionMapper(TaskScheduler scheduler, SpawnListener spawnListener, RegionScanner scanner,
            ListenerRegistrator registrator) {
        this.scheduler = scheduler;
        this.spawnListener = spawnListener;
        this.scanner = scanner;
        if (Version.hasEntitiesLoadEvent()) {
            eventLoadHandler = new One17PlusHandler();
            registrator.register(eventLoadHandler);
        } else {
            eventLoadHandler = null;
        }
    }

    public boolean isBusy() {
        return scheduler.hasRunningTask();
    }

    public CompletableFuture<ColoringResults> dyeEntitiesInRegion(RegionInfo info, RegionOptions options,
            double updateProgress, BiConsumer<Long, Long> updaterConsumer, boolean showScan) {
        CompletableFuture<ColoringResults> future = new CompletableFuture<>();
        ScanReport<?> coloringReport = new MultiReport();
        ScanReport<?> scanReport = showScan ? new MultiReport() : null;
        ColoringResults results = new ColoringResults(coloringReport, scanReport);
        scheduler.scheduleTask(new SimpleRegionTaskDelegator(info,
                (chunk) -> dyeMobInChunk(info, chunk, coloringReport, options, scanReport), () -> {
                    if (eventLoadHandler == null || !eventLoadHandler.hasPendingChunks()) {
                        future.complete(results);
                    } else {
                        eventLoadHandler.reportWhenPendingChunksDone().whenComplete((v, e) -> future.complete(results));
                    }
                }, updateProgress, updaterConsumer));
        return future;
    }

    private void dyeMobInChunk(RegionInfo info, Chunk chunk, ScanReport<?> coloringReport, RegionOptions options,
            ScanReport<?> scanReport) {

        if (eventLoadHandler != null && !chunk.getWorld().isChunkLoaded(chunk)) {
            eventLoadHandler.addChunk(ChunkInfo.wrap(chunk),
                    (entity) -> dealWithEntity(entity, options, info, coloringReport, scanReport), () -> {
                        coloringReport.countAChunk();
                        if (scanReport != null) {
                            scanReport.countAChunk();
                        }
                    });
            chunk.load();
            return;
        } else {
            coloringReport.countAChunk();
            if (scanReport != null) {
                scanReport.countAChunk();
            }
            for (Entity entity : chunk.getEntities()) {
                dealWithEntity(entity, options, info, coloringReport, scanReport);
            }
        }
    }

    private void dealWithEntity(Entity entity, RegionOptions options, RegionInfo info, ScanReport<?> coloringReport,
            ScanReport<?> scanReport) {
        if (isApplicable(entity, options, info)) {
            if (spawnListener.handleEntity((LivingEntity) entity, options, SpawnReason.CUSTOM)) {
                coloringReport.count(entity);
            }
            if (scanReport != null) {
                scanner.countApplicableEntity(entity, scanReport);
            }
        }
    }

    public class ColoringResults {
        private final ScanReport<?> coloringReport;
        private final ScanReport<?> scanReport;

        private ColoringResults(ScanReport<?> coloringReport, ScanReport<?> scanReport) {
            this.coloringReport = coloringReport;
            this.scanReport = scanReport;
        }

        public ScanReport<?> getColoringReport() {
            return coloringReport;
        }

        public ScanReport<?> getScanReport() {
            return scanReport;
        }

        public boolean hasScanReport() {
            return scanReport != null;
        }

    }

}
