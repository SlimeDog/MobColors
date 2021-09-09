package dev.ratas.mobcolors.config.abstraction;

import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class PluginSettingsConfigProvider implements SettingsConfigProvider {
    private final Plugin plugin;

    public PluginSettingsConfigProvider(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public ConfigurationSection getColorSchemesConfig() {
        return plugin.getConfig().getConfigurationSection("color-schemes");
    }

    @Override
    public ConfigurationSection getMobsConfig() {
        return plugin.getConfig().getConfigurationSection("mobs");
    }

    @Override
    public ConfigurationSection getBaseSettingsConfig() {
        return plugin.getConfig();
    }

    @Override
    public Logger getLogger() {
        return plugin.getLogger();
    }

}
