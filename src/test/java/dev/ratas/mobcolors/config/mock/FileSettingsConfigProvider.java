package dev.ratas.mobcolors.config.mock;

import java.io.InputStreamReader;
import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import dev.ratas.mobcolors.config.abstraction.AbstractSettingsConfigProvider;

public class FileSettingsConfigProvider extends AbstractSettingsConfigProvider {
    private final YamlConfiguration config;

    public FileSettingsConfigProvider(FileResourceProvider resourceProvider, Logger logger) {
        super(logger);
        config = YamlConfiguration.loadConfiguration(new InputStreamReader(resourceProvider.getResource("config.yml")));
    }

    @Override
    public ConfigurationSection getBaseSettingsConfig() {
        return config;
    }

}
