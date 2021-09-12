package dev.ratas.mobcolors.config;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;

import dev.ratas.mobcolors.config.abstraction.CustomConfigHandler;
import dev.ratas.mobcolors.config.abstraction.ResourceProvider;
import dev.ratas.mobcolors.config.mob.MobType;
import dev.ratas.mobcolors.utils.WorldDescriptor;

public class Messages extends CustomConfigHandler {
    private static final String NAME = "messages.yml";

    public Messages(ResourceProvider resourceProvider) throws InvalidConfigurationException {
        super(resourceProvider, NAME);
    }

    public String getReloadedMessage() {
        return getMessage("reloaded-config", "Config was successfully reloaded");
    }

    public String getProblemReloadingMessage() {
        return getMessage("problem-reloading-config",
                "There was an issue while reloading the config - check the console log");
    }

    public String getWorldNotFoundMessage(String name) {
        return getMessage("world-not-found", "World not found: {name}").replace("{name}", name);
    }

    public String getNeedANumber(String... vals) {
        String val = String.join(" and ", vals);
        return getMessage("need-a-number", "A number is required, got: {val}").replace("{val}", val);
    }

    public String getMobColorMapDisabledMessage(MobType type) {
        return getMessage("mob-colormap-disabled", "MobColors does not manage {mob} mobs").replace("{mob}",
                type.name());
    }

    public String getStartingToColorRegionMessage(WorldDescriptor world, int x, int z, double updateProgress,
            MobType type) {
        return getMessage("start-coloring-region", "Starting to color {mob} in {world} for region {x}, {z}")
                .replace("{world}", world.getName()).replace("{x}", String.valueOf(x)).replace("{z}", String.valueOf(z))
                .replace("{update-progress}", String.format("%d", (int) updateProgress * 100))
                .replace("{update}", String.valueOf(updateProgress / 20L))
                .replace("{mob}", type == null ? "total" : type.name());
    }

    public String getStartingToColorRadiusMessage(WorldDescriptor world, double distance, double updateProgress,
            MobType type) {
        return getMessage("start-coloring-distance", "Starting to color {mob} for distance {d}")
                .replace("{world}", world.getName()).replace("{d}", String.format("%.2f", distance))
                .replace("{update-progress}", String.format("%d", (int) updateProgress * 100))
                .replace("{update}", String.valueOf(updateProgress / 20L))
                .replace("{mob}", type == null ? "total" : type.name());
    }

    public String getStartingToScanRegionMessage(WorldDescriptor world, int x, int z, double updateProgress) {
        return getMessage("start-scanning-region", "Starting to scan mobs in {world} for region {x}, {z}")
                .replace("{world}", world.getName()).replace("{x}", String.valueOf(x)).replace("{z}", String.valueOf(z))
                .replace("{update-progress}", String.format("%d", (int) updateProgress * 100));
    }

    public String getStartingToScanRadiusMessage(WorldDescriptor world, double distance, double updateProgress,
            MobType type) {
        return getMessage("start-scanning-distance", "Starting to scan {mob} for distance {d}")
                .replace("{world}", world.getName()).replace("{d}", String.format("%.2f", distance))
                .replace("{update-progress}", String.format("%d", (int) updateProgress * 100))
                .replace("{mob}", type == null ? "total" : type.name());
    }

    public String getUpdateOnColorRegionMessage(long done, long total) {
        String percent = String.valueOf((done * 100) / total);
        return getMessage("update-coloring-region", "{done}/{total} chunks colored ({percent}%)")
                .replace("{done}", String.valueOf(done)).replace("{total}", String.valueOf(total))
                .replace("{percent}", String.valueOf(percent));
    }

    public String getUpdateOnColorRadiusMessage(long done, long total) {
        String percent = String.valueOf((done * 100) / total);
        return getMessage("update-coloring-distance", "{done}/{total} chunks colored ({percent}%)")
                .replace("{done}", String.valueOf(done)).replace("{total}", String.valueOf(total))
                .replace("{percent}", String.valueOf(percent));
    }

    public String getUpdateOnScanRegionMessage(long done, long total) {
        String percent = String.valueOf((done * 100) / total);
        return getMessage("update-scanning-region", "{done}/{total} chunks scanned ({percent}%)")
                .replace("{done}", String.valueOf(done)).replace("{total}", String.valueOf(total))
                .replace("{percent}", String.valueOf(percent));
    }

    public String getUpdateOnScanRadiusMessage(long done, long total) {
        String percent = String.valueOf((done * 100) / total);
        return getMessage("update-scanning-distance", "{done}/{total} chunks scanned ({percent}%)")
                .replace("{done}", String.valueOf(done)).replace("{total}", String.valueOf(total))
                .replace("{percent}", String.valueOf(percent));
    }

    public String getDoneColoringRegionMessage(long count, long chunks) {
        return getMessage("done-coloring-region", "Coloring completed. Colored {count} mobs in {chunks} loaded chunks")
                .replace("{count}", String.valueOf(count)).replace("{chunks}", String.valueOf(chunks));
    }

    public String getDoneColoringRadiusMessage(long count, long chunks, double distance) {
        return getMessage("done-coloring-distance", "Coloring completed. Colored {count} mobs in distance {d}")
                .replace("{count}", String.valueOf(count)).replace("{chunks}", String.valueOf(chunks))
                .replace("{d}", String.format("%.2f", distance));
    }

    public String getDoneScanningHeaderMessage(long count, long chunks, MobType type) {
        return getMessage("done-scanning-header",
                "Scanning completed. Scanned {count} {type} in {chunks} loaded chunks")
                        .replace("{count}", String.valueOf(count)).replace("{chunks}", String.valueOf(chunks))
                        .replace("{type}", type == null ? "total" : type.name());
    }

    public String getDoneScanningItemMessage(Object color, int amount) {
        String msg = getMessage("done-scanning-item", "- {color}: {amount}").replace("{amount}",
                String.valueOf(amount));
        if (color instanceof Enum) {
            String enumName = ((Enum<?>) color).name().toLowerCase();
            return msg.replace("{color}", enumName);
        } else if (color == null) { // shulker special case with default
            return msg.replace("{color}", "default");
        } else {
            if (color instanceof HorseVariant) {
                return msg.replace("{color}", ((HorseVariant) color).getName().toLowerCase());
            } else if (color instanceof TropicalFishVariant) {
                return msg.replace("{color}", ((TropicalFishVariant) color).getName().toLowerCase());
            } else {
                throw new IllegalArgumentException("Unknown color type: " + color);
            }
        }
    }

    public String getNoColormapsEnabledMessage() {
        return getMessage("no-colormaps-enabled", "You currently have no color-schemes enabled in any world");
    }

    public String getEnabledMobColorMapsHeaderMessage(MobType type) {
        return getMessage("info-mob-header", "Enabled color-schemes for {type}:").replace("{type}", type.name());
    }

    public String getEnabledMobColorMapItemMessage(String name, List<String> worlds) {
        return getMessage("info-mob-colormap-item", "- {name}: {worlds}").replace("{name}", name).replace("{worlds}",
                String.join(", ", worlds));
    }

    public String getMobColorMapDefaultEnabledMessage() {
        return getMessage("info-mob-colormap-default-enabled", "- default: all other worlds");
    }

    public String getMobColorMapDefaultDisabledMessage() {
        return getMessage("info-mob-colormap-default-disabled", "- vanilla default in all other worlds");
    }

    public String getNoColorMapsInWorldMessage(WorldDescriptor world) {
        return getMessage("info-no-colormaps-in-world", "There are no color-schemes enabled in world {world}")
                .replace("{world}", world.getName());
    }

    public String getWorldColorMapsHeaderMessage(WorldDescriptor world) {
        return getMessage("info-world-header", "The following color-schemes have been enabled in {world}:")
                .replace("{world}", world.getName());
    }

    public String getWorldColorMapItemMessage(MobType type, String name) {
        return getMessage("info-colormap-in-world", "- {type}: {name}").replace("{type}", type.name()).replace("{name}",
                name);
    }

    public String getScanDistanceTooBig(double maxDistance) {
        return getMessage("scan-distance-too-big",
                "You are trying to scan an area that is too large. The maximum distance allowed is {max-distance}.")
                        .replace("{max-distance}", String.format("%.0f", maxDistance));
    }

    public String getColorDistanceTooBig(double maxDistance) {
        return getMessage("color-distance-too-big",
                "You are trying to color an area that is too large. The maximum distance allowed is {max-distance}.")
                        .replace("{max-distance}", String.format("%.0f", maxDistance));
    }

    public String getSchedulerBusyMessage() {
        return getMessage("scheduler-busy", "The task scheduler is busy right now. Please try again later.");
    }

    public String updateCurrentVersion() {
        return getMessage("updater-current-version", "You are running the latest version");
    }

    public String updateNewVersionAvailable(String version) {
        return getMessage("updater-new-version", "A new version {version} is available for download")
                .replace("{version}", version);
    }

    public String updateInfoUnavailable() {
        return getMessage("updater-info-unavailable", "Version update information is not available at this time");
    }

    private String getMessage(String path, String def) {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString(path, def));
    }

}