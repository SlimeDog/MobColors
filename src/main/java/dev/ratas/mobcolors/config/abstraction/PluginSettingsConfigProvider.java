package dev.ratas.mobcolors.config.abstraction;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class PluginSettingsConfigProvider extends AbstractSettingsConfigProvider {
    private final Plugin plugin;

    public PluginSettingsConfigProvider(Plugin plugin) {
        super(plugin.getLogger());
        this.plugin = plugin;
    }

    @Override
    public ConfigurationSection getBaseSettingsConfig() {
        return plugin.getConfig();
    }

}
