package dev.ratas.mobcolors.config.abstraction;

import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;

public interface SettingsConfigProvider {

    ConfigurationSection getColorSchemesConfig();

    ConfigurationSection getMobsConfig();

    ConfigurationSection getBaseSettingsConfig();

    Logger getLogger();

}
