package dev.ratas.mobcolors.utils;

import org.bukkit.plugin.java.JavaPlugin;

public class VersionProvider {
    private final JavaPlugin plugin;

    public VersionProvider(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

}
