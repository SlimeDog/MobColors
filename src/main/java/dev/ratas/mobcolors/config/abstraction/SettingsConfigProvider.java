package dev.ratas.mobcolors.config.abstraction;

import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class SettingsConfigProvider {
    private final Plugin plugin;

    public SettingsConfigProvider(Plugin plugin) {
        this.plugin = plugin;
    }

    public ConfigurationSection getColorSchemesConfig() {
        return plugin.getConfig().getConfigurationSection("color-schemes");
    }

    public ConfigurationSection getMobsConfig() {
        return plugin.getConfig().getConfigurationSection("mobs");
    }

    public ConfigurationSection getBaseSettingsConfig() {
        return plugin.getConfig();
    }

    public Logger getLogger() {
        return plugin.getLogger();
    }

}
