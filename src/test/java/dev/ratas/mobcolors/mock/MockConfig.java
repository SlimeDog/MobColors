package dev.ratas.mobcolors.mock;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import dev.ratas.slimedogcore.api.config.SDCConfiguration;
import dev.ratas.slimedogcore.api.config.SDCCustomConfig;
import dev.ratas.slimedogcore.api.config.exceptions.ConfigReloadException;
import dev.ratas.slimedogcore.api.config.exceptions.ConfigSaveException;
import dev.ratas.slimedogcore.impl.config.ConfigurationWrapper;

public class MockConfig implements SDCCustomConfig {
    private final File file;
    private final ConfigurationWrapper wrapper;

    public MockConfig(File file) {
        this.file = file;
        wrapper = new ConfigurationWrapper(YamlConfiguration.loadConfiguration(file));
    }

    @Override
    public void reloadConfig() throws ConfigReloadException {
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public SDCConfiguration getConfig() {
        return wrapper;
    }

    @Override
    public void saveConfig() throws ConfigSaveException {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveDefaultConfig() throws ConfigSaveException {
        // TODO Auto-generated method stub

    }

}
