package dev.ratas.mobcolors.config.abstraction;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import dev.ratas.mobcolors.utils.LogUtils;

public class PluginResourceProvider implements ResourceProvider {
    private final JavaPlugin plugin;

    public PluginResourceProvider(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public File getDataFolder() {
        return plugin.getDataFolder();
    }

    public InputStream getResource(String filename) {
        return plugin.getResource(filename);
    }

    public void saveResource(String filename, boolean force) {
        plugin.saveResource(filename, force);
    }

    public Logger getLogger() {
        return LogUtils.getLogger();
    }
}
