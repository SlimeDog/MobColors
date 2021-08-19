package dev.ratas.mobcolors.config;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;

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

    public String getStartingToColorMessage(World world, int x, int z, long updateTicks) {
        return getMessage("start-coloring",
                "Starting to color sheep in {world} for region {x}, {z} (updates every {update-ticks} ticks)")
                        .replace("{world}", world.getName()).replace("{x}", String.valueOf(x))
                        .replace("{z}", String.valueOf(z)).replace("{update-ticks}", String.valueOf(updateTicks))
                        .replace("{update}", String.valueOf(updateTicks / 20L));
    }

    public String getStartingToScanMessage(World world, int x, int z, long updateTicks) {
        return getMessage("start-scanning",
                "Starting to scan sheep in {world} for region {x}, {z} (updates every {update-ticks} ticks)")
                        .replace("{world}", world.getName()).replace("{x}", String.valueOf(x))
                        .replace("{z}", String.valueOf(z)).replace("{update-ticks}", String.valueOf(updateTicks))
                        .replace("{update}", String.valueOf(updateTicks / 20L));
    }

    public String getUpdateOnColorMessage(long done, long total) {
        String percent = String.valueOf((done * 100) / total);
        return getMessage("update-coloring", "{done}/{total} chunks colored ({percent}%)")
                .replace("{done}", String.valueOf(done)).replace("{total}", String.valueOf(total))
                .replace("{percent}", String.valueOf(percent));
    }

    public String getUpdateOnScanMessage(long done, long total) {
        String percent = String.valueOf((done * 100) / total);
        return getMessage("update-scanning", "{done}/{total} chunks scanned ({percent}%)")
                .replace("{done}", String.valueOf(done)).replace("{total}", String.valueOf(total))
                .replace("{percent}", String.valueOf(percent));
    }

    public String getDoneColoringMessage(long sheep, long chunks) {
        return getMessage("done-coloring", "The coloring was done. Colored {sheep} sheep in {chunks} chunks")
                .replace("{sheep}", String.valueOf(sheep)).replace("{chunks}", String.valueOf(chunks));
    }

    public String getDoneScanningHeaderMessage(long sheep, long chunks) {
        return getMessage("done-scanning-header", "The scanning was done. Scanned {sheep} sheep in {chunks} chunks")
                .replace("{sheep}", String.valueOf(sheep)).replace("{chunks}", String.valueOf(chunks));
    }

    public String getDoneScanningItemMessage(DyeColor color, int amount) {
        return getMessage("done-scanning-item", "{color}: {amount}").replace("{color}", color.name())
                .replace("{amount}", String.valueOf(amount));
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