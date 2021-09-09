package dev.ratas.mobcolors.config.abstraction;

import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;

public abstract class AbstractSettingsConfigProvider implements SettingsConfigProvider {
    private final Logger logger;

    protected AbstractSettingsConfigProvider(Logger logger) {
        this.logger = logger;
    }

    @Override
    public ConfigurationSection getColorSchemesConfig() {
        return getBaseSettingsConfig().getConfigurationSection("color-schemes");
    }

    @Override
    public ConfigurationSection getMobsConfig() {
        return getBaseSettingsConfig().getConfigurationSection("mobs");
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

}
