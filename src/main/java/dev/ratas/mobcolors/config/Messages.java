package dev.ratas.mobcolors.config;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.EntityType;

import dev.ratas.mobcolors.config.abstraction.CustomConfigHandler;
import dev.ratas.mobcolors.config.abstraction.ResourceProvider;

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

    public String getStartingToColorRegionMessage(World world, int x, int z, long updateTicks, EntityType type) {
        return getMessage("start-coloring-region",
                "Starting to color {mob} in {world} for region {x}, {z} (updates every {update-ticks} ticks)")
                        .replace("{world}", world.getName()).replace("{x}", String.valueOf(x))
                        .replace("{z}", String.valueOf(z)).replace("{update-ticks}", String.valueOf(updateTicks))
                        .replace("{update}", String.valueOf(updateTicks / 20L))
                        .replace("{mob}", type == null ? "ALL" : type.name().toLowerCase());
    }

    public String getStartingToColorRadiusMessage(World world, double distance, long updateTicks, EntityType type) {
        return getMessage("start-coloring-radius",
                "Starting to color {mob} for radius {r} (updates every {update-ticks} ticks)")
                        .replace("{world}", world.getName()).replace("{r}", String.format("%2.f", distance))
                        .replace("{update-ticks}", String.valueOf(updateTicks))
                        .replace("{update}", String.valueOf(updateTicks / 20L))
                        .replace("{mob}", type == null ? "ALL" : type.name().toLowerCase());
    }

    public String getStartingToScanRegionMessage(World world, int x, int z, long updateTicks) {
        return getMessage("start-scanning-region",
                "Starting to scan mobs in {world} for region {x}, {z} (updates every {update-ticks} ticks)")
                        .replace("{world}", world.getName()).replace("{x}", String.valueOf(x))
                        .replace("{z}", String.valueOf(z)).replace("{update-ticks}", String.valueOf(updateTicks))
                        .replace("{update}", String.valueOf(updateTicks / 20L));
    }

    public String getStartingToScanRadiusMessage(World world, double distance, long updateTicks, EntityType type) {
        return getMessage("start-scanning-radius",
                "Starting to scan {mob} for radius {r} (updates every {update-ticks} ticks)")
                        .replace("{world}", world.getName()).replace("{r}", String.format("%2.f", distance))
                        .replace("{update-ticks}", String.valueOf(updateTicks))
                        .replace("{update}", String.valueOf(updateTicks / 20L))
                        .replace("{mob}", type == null ? "ALL" : type.name().toLowerCase());
    }

    public String getUpdateOnColorRegionMessage(long done, long total) {
        String percent = String.valueOf((done * 100) / total);
        return getMessage("update-coloring-region", "{done}/{total} chunks colored ({percent}%)")
                .replace("{done}", String.valueOf(done)).replace("{total}", String.valueOf(total))
                .replace("{percent}", String.valueOf(percent));
    }

    public String getUpdateOnColorRadiusMessage(long done, long total) {
        String percent = String.valueOf((done * 100) / total);
        return getMessage("update-coloring-radius", "{done}/{total} chunks colored ({percent}%)")
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
        return getMessage("update-scanning-radius", "{done}/{total} chunks scanned ({percent}%)")
                .replace("{done}", String.valueOf(done)).replace("{total}", String.valueOf(total))
                .replace("{percent}", String.valueOf(percent));
    }

    public String getDoneColoringRegionMessage(long count, long chunks) {
        return getMessage("done-coloring-region", "Coloring completed. Colored {count} mobs in {chunks} loaded chunks")
                .replace("{count}", String.valueOf(count)).replace("{chunks}", String.valueOf(chunks));
    }

    public String getDoneColoringRadiusMessage(long count, long chunks, double distance) {
        return getMessage("done-coloring-radius", "Coloring completed. Colored {count} mobs in radius {r}")
                .replace("{count}", String.valueOf(count)).replace("{chunks}", String.valueOf(chunks))
                .replace("{r}", String.format("%2.f", distance));
    }

    public String getDoneScanningHeaderMessage(long count, long chunks, EntityType type) {
        return getMessage("done-scanning-header", "The scanning was done. Scanned {count} {type} in {chunks} chunks")
                .replace("{count}", String.valueOf(count)).replace("{chunks}", String.valueOf(chunks))
                .replace("{type}", type == null ? "ALL" : type.name().toLowerCase());
    }

    public String getDoneScanningItemMessage(Object color, int amount) {
        String msg = getMessage("done-scanning-item", "{color}: {amount}").replace("{amount}", String.valueOf(amount));
        if (color instanceof Enum) {
            return msg.replace("{color}", ((Enum<?>) color).name());
        } else {
            if (color instanceof HorseVariant) {
                return msg.replace("{color}", ((HorseVariant) color).getName());
            } else if (color instanceof TropicalFishVariant) {
                return msg.replace("{color}", ((TropicalFishVariant) color).getName());
            } else {
                throw new IllegalArgumentException("Unknown color type: " + color);
            }
        }
    }

    public String updateCurrentVersion() {
        return getMessage("updater-current-version", "You are running the latest version");
    }

    public String updateNewVersionAvailable(String version) {
        return getMessage("updater-new-version", "A new version {VERSION} is available for download")
                .replace("{VERSION}", version);
    }

    public String updateInfoUnavailable() {
        return getMessage("updater-info-unavailable", "Version update information is not available at this time");
    }

    private String getMessage(String path, String def) {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString(path, def));
    }

}