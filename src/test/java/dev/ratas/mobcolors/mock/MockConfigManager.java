package dev.ratas.mobcolors.mock;

import java.io.File;

import dev.ratas.slimedogcore.api.config.SDCCustomConfig;
import dev.ratas.slimedogcore.api.config.SDCCustomConfigManager;

public class MockConfigManager implements SDCCustomConfigManager {
    private final SDCCustomConfig config;

    public MockConfigManager(SDCCustomConfig config) {
        this.config = config;
    }

    @Override
    public SDCCustomConfig getConfig(String fileName) {
        return config;
    }

    @Override
    public SDCCustomConfig getConfig(File file) {
        return config;
    }

    @Override
    public SDCCustomConfig getDefaultConfig() {
        return config;
    }

}
