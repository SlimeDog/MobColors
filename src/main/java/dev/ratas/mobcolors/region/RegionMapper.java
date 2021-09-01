package dev.ratas.mobcolors.region;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import dev.ratas.mobcolors.SpawnListener;
import dev.ratas.mobcolors.scheduling.SimpleRegionTaskDelegator;
import dev.ratas.mobcolors.scheduling.TaskScheduler;

public class RegionMapper extends AbstractRegionHandler {
    private final TaskScheduler scheduler;
    private final SpawnListener spawnListener;
    private final RegionScanner scanner;

    public RegionMapper(TaskScheduler scheduler, SpawnListener spawnListener, RegionScanner scanner) {
        this.scheduler = scheduler;
        this.spawnListener = spawnListener;
        this.scanner = scanner;
    }

    public boolean isBusy() {
        return scheduler.hasRunningTask();
    }

    public CompletableFuture<ColoringResults> dyeEntitiesInRegion(RegionInfo info, RegionOptions options,
            double updateProgress, BiConsumer<Long, Long> updaterConsumer, boolean showScan) {
        CompletableFuture<ColoringResults> future = new CompletableFuture<>();
        ScanReport<?> report = new MultiReport();
        ScanReport<?> scanReport = showScan ? new MultiReport() : null;
        ColoringResults results = new ColoringResults(report, scanReport);
        scheduler.scheduleTask(
                new SimpleRegionTaskDelegator(info, (chunk) -> dyeMobInChunk(info, chunk, report, options, scanReport),
                        () -> future.complete(results), updateProgress, updaterConsumer));
        return future;
    }

    private void dyeMobInChunk(RegionInfo info, Chunk chunk, ScanReport<?> report, RegionOptions options,
            ScanReport<?> scanReport) {
        report.countAChunk();
        if (scanReport != null) {
            scanReport.countAChunk();
        }
        for (Entity entity : chunk.getEntities()) {
            dealWithEntity(entity, options, info, report, scanReport);
        }
    }

    private void dealWithEntity(Entity entity, RegionOptions options, RegionInfo info, ScanReport<?> report,
            ScanReport<?> scanReport) {
        if (isApplicable(entity, options, info)) {
            if (spawnListener.handleEntity((LivingEntity) entity, options, SpawnReason.CUSTOM)) {
                report.count(entity);
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
