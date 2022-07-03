package dev.ratas.mobcolors.config;

import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;

import dev.ratas.mobcolors.config.mob.MobType;
import dev.ratas.mobcolors.config.variants.TropicalFishVariant;
import dev.ratas.mobcolors.utils.WorldDescriptor;
import dev.ratas.slimedogcore.api.SlimeDogPlugin;
import dev.ratas.slimedogcore.api.messaging.factory.SDCDoubleContextMessageFactory;
import dev.ratas.slimedogcore.api.messaging.factory.SDCQuadrupleContextMessageFactory;
import dev.ratas.slimedogcore.api.messaging.factory.SDCSingleContextMessageFactory;
import dev.ratas.slimedogcore.api.messaging.factory.SDCTripleContextMessageFactory;
import dev.ratas.slimedogcore.api.messaging.factory.SDCVoidContextMessageFactory;
import dev.ratas.slimedogcore.impl.messaging.MessagesBase;
import dev.ratas.slimedogcore.impl.messaging.factory.MsgUtil;

public class Messages extends MessagesBase {
    private static final String NAME = "messages.yml";
    private SDCVoidContextMessageFactory reloadMessage;
    private SDCVoidContextMessageFactory problemReloading;
    private SDCSingleContextMessageFactory<String> worldNotFound;
    private SDCSingleContextMessageFactory<String[]> needNumber;
    private SDCSingleContextMessageFactory<MobType> mobColorMapDisabled;
    private SDCQuadrupleContextMessageFactory<WorldDescriptor, Integer, Integer, MobType> startingToColorRegion;
    private SDCTripleContextMessageFactory<WorldDescriptor, Double, MobType> startingToColorDistance;
    private SDCQuadrupleContextMessageFactory<WorldDescriptor, Integer, Integer, MobType> startingToScanRegion;
    private SDCTripleContextMessageFactory<WorldDescriptor, Double, MobType> startingToScanDistance;
    private SDCSingleContextMessageFactory<ProgressInfo> updateRegion;
    private SDCSingleContextMessageFactory<ProgressInfo> updateDistance;
    private SDCSingleContextMessageFactory<ProgressInfo> updateRegionScan;
    private SDCSingleContextMessageFactory<ProgressInfo> updateDistanceScan;
    private SDCDoubleContextMessageFactory<Long, Long> doneColoringRegion;
    private SDCTripleContextMessageFactory<Long, Long, Double> doneColoringDistance;
    private SDCTripleContextMessageFactory<Long, Long, MobType> doneScanningHeader;
    private SDCDoubleContextMessageFactory<Integer, Object> doneScanningItem;
    private SDCVoidContextMessageFactory noColorMapsEnabled;
    private SDCSingleContextMessageFactory<MobType> enabledColorMapsHeader;
    private SDCDoubleContextMessageFactory<String, List<String>> enabledColorMapItem;
    private SDCVoidContextMessageFactory colorMapDefaultElsewhere;
    private SDCVoidContextMessageFactory colorMapDefaultEverywhere;
    private SDCVoidContextMessageFactory colorMapDefaultDisabled;
    private SDCSingleContextMessageFactory<WorldDescriptor> noColorMapsInWorld;
    private SDCSingleContextMessageFactory<WorldDescriptor> worldColorMapHeader;
    private SDCDoubleContextMessageFactory<MobType, String> worldColorMapItem;
    private SDCSingleContextMessageFactory<Double> scanDistanceTooBig;
    private SDCSingleContextMessageFactory<Double> colorDistanceTooBig;
    private SDCVoidContextMessageFactory schedulerBusy;
    private SDCSingleContextMessageFactory<String> mobTypeNotAvailable;
    private SDCVoidContextMessageFactory updateCurrentVersion;
    private SDCSingleContextMessageFactory<String> updateNewVersion;
    private SDCVoidContextMessageFactory updateInfoUnavailable;

    public Messages(SlimeDogPlugin plugin) throws InvalidConfigurationException {
        super(plugin.getCustomConfigManager().getConfig(NAME));
        loadMessages();
    }

    private void loadMessages() {
        reloadMessage = MsgUtil
                .voidContext(getRawMessage("reloaded-config", "Config was successfully reloaded"));
        problemReloading = MsgUtil.voidContext(getRawMessage("problem-reloading-config",
                "There was an issue while reloading the config - check the console log"));
        worldNotFound = MsgUtil.singleContext("{name}", name -> name,
                getRawMessage("world-not-found", "World not found: {name}"));
        needNumber = MsgUtil.singleContext("{val}", nrs -> String.join(" and ", nrs),
                getRawMessage("need-a-number", "A number is required, got: {val}"));
        mobColorMapDisabled = MsgUtil.singleContext("{mob}", type -> type.name(),
                getRawMessage("mob-colormap-disabled", "MobColors does not manage {mob} mobs"));
        startingToColorRegion = MsgUtil.quadrupleContext("{world}", wd -> wd.getName(), "{x}", x -> valueOf(x),
                "{z}", z -> valueOf(z), "{mob}", type -> type == null ? "total" : type.name(),
                getRawMessage("start-coloring-region", "Starting to color {mob} in {world} for region {x}, {z}"));
        startingToColorDistance = MsgUtil.tripleContext("{world}", wd -> wd.getName(), "{d}",
                dist -> String.format("%.2f", dist), "{mob}", type -> type == null ? "total" : type.name(),
                getRawMessage("start-coloring-distance", "Starting to color {mob} for distance {d}"));
        startingToScanRegion = MsgUtil.quadrupleContext("{world}", wd -> wd.getName(), "{x}", x -> valueOf(x),
                "{z}", z -> valueOf(z), "{mob}", type -> type == null ? "total" : type.name(),
                getRawMessage("start-scanning-region", "Starting to scan mobs in {world} for region {x}, {z}"));
        startingToScanDistance = MsgUtil.tripleContext("{world}", wd -> wd.getName(), "{d}",
                dist -> String.format("%.2f", dist), "{mob}", type -> type == null ? "total" : type.name(),
                getRawMessage("start-scanning-distance", "Starting to scan {mob} for distance {d}"));
        MsgUtil.MultipleToOneBuilder<ProgressInfo> updateRegionBuilder = new MsgUtil.MultipleToOneBuilder<>(
                getRawMessage("update-coloring-region", "{done}/{total} chunks colored ({percent}%)"));
        updateRegionBuilder.with("{done}", pi -> valueOf(pi.done()));
        updateRegionBuilder.with("{total}", pi -> valueOf(pi.total()));
        updateRegionBuilder.with("{percent}", pi -> valueOf((pi.done() * 100) / pi.total()));
        updateRegion = updateRegionBuilder.build();
        MsgUtil.MultipleToOneBuilder<ProgressInfo> updateDistanceBuilder = new MsgUtil.MultipleToOneBuilder<>(
                getRawMessage("update-coloring-distance", "{done}/{total} chunks colored ({percent}%)"));
        updateDistanceBuilder.with("{done}", pi -> valueOf(pi.done()));
        updateDistanceBuilder.with("{total}", pi -> valueOf(pi.total()));
        updateDistanceBuilder.with("{percent}", pi -> valueOf((pi.done() * 100) / pi.total()));
        updateDistance = updateDistanceBuilder.build();
        MsgUtil.MultipleToOneBuilder<ProgressInfo> updateRegionScanBuilder = new MsgUtil.MultipleToOneBuilder<>(
                getRawMessage("update-scanning-region", "{done}/{total} chunks scanned ({percent}%)"));
        updateRegionScanBuilder.with("{done}", pi -> valueOf(pi.done()));
        updateRegionScanBuilder.with("{total}", pi -> valueOf(pi.total()));
        updateRegionScanBuilder.with("{percent}", pi -> valueOf((pi.done() * 100) / pi.total()));
        updateRegionScan = updateRegionScanBuilder.build();
        MsgUtil.MultipleToOneBuilder<ProgressInfo> updateDistanceScanBuilder = new MsgUtil.MultipleToOneBuilder<>(
                getRawMessage("update-scanning-distance", "{done}/{total} chunks scanned ({percent}%)"));
        updateDistanceScanBuilder.with("{done}", pi -> valueOf(pi.done()));
        updateDistanceScanBuilder.with("{total}", pi -> valueOf(pi.total()));
        updateDistanceScanBuilder.with("{percent}", pi -> valueOf((pi.done() * 100) / pi.total()));
        updateDistanceScan = updateDistanceScanBuilder.build();
        doneColoringRegion = MsgUtil.doubleContext("{count}", count -> valueOf(count), "{chunks}",
                chunks -> valueOf(chunks), getRawMessage("done-coloring-region",
                        "Coloring completed. Colored {count} mobs in {chunks} loaded chunks"));
        doneColoringDistance = MsgUtil.tripleContext("{count}", count -> valueOf(count), "{chunks}",
                chunks -> valueOf(chunks), "{d}", dist -> String.format("%.2f", dist),
                getRawMessage("done-coloring-distance", "Coloring completed. Colored {count} mobs in distance {d}"));
        doneScanningHeader = MsgUtil.tripleContext("{count}", count -> valueOf(count), "{chunks}",
                chunks -> valueOf(chunks), "{type}", type -> type.name(), getRawMessage("done-scanning-header",
                        "Scanning completed. Scanned {count} {type} in {chunks} loaded chunks"));
        doneScanningItem = MsgUtil.doubleContext("{amount}", amount -> valueOf(amount), "{color}", color -> {
            if (color instanceof Enum) {
                return ((Enum<?>) color).name().toLowerCase();
            } else if (color == null) { // shulker special case with default
                return "default";
            } else {
                if (color instanceof HorseVariant) {
                    return ((HorseVariant) color).getName().toLowerCase();
                } else if (color instanceof TropicalFishVariant) {
                    return ((TropicalFishVariant) color).getName().toLowerCase();
                } else {
                    throw new IllegalColorException(color);
                }
            }
        }, getRawMessage("done-scanning-item", "- {color}: {amount}"));
        noColorMapsEnabled = MsgUtil.voidContext(
                getRawMessage("no-colormaps-enabled", "You currently have no color-schemes enabled in any world"));
        enabledColorMapsHeader = MsgUtil.singleContext("{type}", type -> type.name(),
                getRawMessage("info-mob-header", "Enabled color-schemes for {type}:"));
        enabledColorMapItem = MsgUtil.doubleContext("{name}", name -> name, "{worlds}",
                worlds -> String.join(", ", worlds), getRawMessage("info-mob-colormap-item", "- {name}: {worlds}"));
        colorMapDefaultElsewhere = MsgUtil
                .voidContext(getRawMessage("info-mob-colormap-default-everywhere", "- default: all worlds"));
        colorMapDefaultEverywhere = MsgUtil
                .voidContext(getRawMessage("info-mob-colormap-default-enabled", "- default: all other worlds"));
        colorMapDefaultDisabled = MsgUtil.voidContext(
                getRawMessage("info-mob-colormap-default-disabled", "- vanilla default in all other worlds"));
        noColorMapsInWorld = MsgUtil.singleContext("{world}", world -> world.getName(),
                getRawMessage("info-no-colormaps-in-world", "There are no color-schemes enabled in world {world}"));
        worldColorMapHeader = MsgUtil.singleContext("{world}", world -> world.getName(),
                getRawMessage("info-world-header", "The following color-schemes have been enabled in {world}:"));
        worldColorMapItem = MsgUtil.doubleContext("{type}", type -> type.name(), "{name}", name -> name,
                getRawMessage("info-colormap-in-world", "- {type}: {name}"));
        scanDistanceTooBig = MsgUtil.singleContext("{max-distance}", maxDistance -> String.format("%.0f", maxDistance),
                getRawMessage("scan-distance-too-big",
                        "You are trying to scan an area that is too large. The maximum distance allowed is {max-distance}."));
        colorDistanceTooBig = MsgUtil.singleContext("{max-distance}", maxDistance -> String.format("%.0f", maxDistance),
                getRawMessage("color-distance-too-big",
                        "You are trying to color an area that is too large. The maximum distance allowed is {max-distance}."));
        schedulerBusy = MsgUtil.voidContext(
                getRawMessage("scheduler-busy", "The task scheduler is busy right now. Please try again later."));
        mobTypeNotAvailable = MsgUtil.singleContext("{type}", typeName -> typeName, getRawMessage(
                "mob-type-not-available",
                "Mob type {type} is not valid in this version of Minecraft. It should be disabled in the configuration."));
        updateCurrentVersion = MsgUtil
                .voidContext(getRawMessage("updater-current-version", "You are running the latest version"));
        updateNewVersion = MsgUtil.singleContext("{version}", version -> version,
                getRawMessage("updater-new-version", "A new version {version} is available for download"));
        updateInfoUnavailable = MsgUtil.voidContext(
                getRawMessage("updater-info-unavailable", "Version update information is not available at this time"));
    }

    public SDCVoidContextMessageFactory getReloadedMessage() {
        return reloadMessage;
    }

    public SDCVoidContextMessageFactory getProblemReloadingMessage() {
        return problemReloading;
    }

    public SDCSingleContextMessageFactory<String> getWorldNotFoundMessage() {
        return worldNotFound;
    }

    public SDCSingleContextMessageFactory<String[]> getNeedANumber() {
        return needNumber;
    }

    public SDCSingleContextMessageFactory<MobType> getMobColorMapDisabledMessage() {
        return mobColorMapDisabled;
    }

    public SDCQuadrupleContextMessageFactory<WorldDescriptor, Integer, Integer, MobType> getStartingToColorRegionMessage() {
        return startingToColorRegion;
    }

    public SDCTripleContextMessageFactory<WorldDescriptor, Double, MobType> getStartingToColorRadiusMessage() {
        return startingToColorDistance;
    }

    public SDCQuadrupleContextMessageFactory<WorldDescriptor, Integer, Integer, MobType> getStartingToScanRegionMessage() {
        return startingToScanRegion;
    }

    public SDCTripleContextMessageFactory<WorldDescriptor, Double, MobType> getStartingToScanRadiusMessage() {
        return startingToScanDistance;
    }

    public SDCSingleContextMessageFactory<ProgressInfo> getUpdateOnColorRegionMessage() {
        return updateRegion;
    }

    public SDCSingleContextMessageFactory<ProgressInfo> getUpdateOnColorRadiusMessage() {
        return updateDistance;
    }

    public SDCSingleContextMessageFactory<ProgressInfo> getUpdateOnScanRegionMessage() {
        return updateRegionScan;
    }

    public SDCSingleContextMessageFactory<ProgressInfo> getUpdateOnScanRadiusMessage() {
        return updateDistanceScan;
    }

    public SDCDoubleContextMessageFactory<Long, Long> getDoneColoringRegionMessage() {
        return doneColoringRegion;
    }

    public SDCTripleContextMessageFactory<Long, Long, Double> getDoneColoringRadiusMessage() {
        return doneColoringDistance;
    }

    public SDCTripleContextMessageFactory<Long, Long, MobType> getDoneScanningHeaderMessage() {
        return doneScanningHeader;
    }

    public SDCDoubleContextMessageFactory<Integer, Object> getDoneScanningItemMessage() {
        return doneScanningItem;
    }

    public SDCVoidContextMessageFactory getNoColormapsEnabledMessage() {
        return noColorMapsEnabled;
    }

    public SDCSingleContextMessageFactory<MobType> getEnabledMobColorMapsHeaderMessage() {
        return enabledColorMapsHeader;
    }

    public SDCDoubleContextMessageFactory<String, List<String>> getEnabledMobColorMapItemMessage() {
        return enabledColorMapItem;
    }

    public SDCVoidContextMessageFactory getMobColorMapDefaultEnabledEverywherMessage() {
        return colorMapDefaultElsewhere;
    }

    public SDCVoidContextMessageFactory getMobColorMapDefaultEnabledMessage() {
        return colorMapDefaultEverywhere;
    }

    public SDCVoidContextMessageFactory getMobColorMapDefaultDisabledMessage() {
        return colorMapDefaultDisabled;
    }

    public SDCSingleContextMessageFactory<WorldDescriptor> getNoColorMapsInWorldMessage() {
        return noColorMapsInWorld;
    }

    public SDCSingleContextMessageFactory<WorldDescriptor> getWorldColorMapsHeaderMessage() {
        return worldColorMapHeader;
    }

    public SDCDoubleContextMessageFactory<MobType, String> getWorldColorMapItemMessage() {
        return worldColorMapItem;
    }

    public SDCSingleContextMessageFactory<Double> getScanDistanceTooBig() {
        return scanDistanceTooBig;
    }

    public SDCSingleContextMessageFactory<Double> getColorDistanceTooBig() {
        return colorDistanceTooBig;
    }

    public SDCVoidContextMessageFactory getSchedulerBusyMessage() {
        return schedulerBusy;
    }

    public SDCSingleContextMessageFactory<String> getMobTypeNotAvailable() {
        return mobTypeNotAvailable;
    }

    public SDCVoidContextMessageFactory updateCurrentVersion() {
        return updateCurrentVersion;
    }

    public SDCSingleContextMessageFactory<String> updateNewVersionAvailable() {
        return updateNewVersion;
    }

    public SDCVoidContextMessageFactory updateInfoUnavailable() {
        return updateInfoUnavailable;
    }

    public static final record ProgressInfo(long done, long total) {

    }

    private static String valueOf(Object o) {
        return String.valueOf(o);
    }

    public static final class IllegalColorException extends IllegalArgumentException {

        public IllegalColorException(Object color) {
            super("Unknown color type: " + color);
        }

    }

}