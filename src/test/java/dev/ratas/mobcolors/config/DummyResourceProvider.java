package dev.ratas.mobcolors.config;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

import dev.ratas.mobcolors.config.abstraction.ResourceProvider;

public class DummyResourceProvider implements ResourceProvider {
    private final Logger logger;
    private final File dataFolder;

    DummyResourceProvider(Logger logger) {
        this.logger = logger;
        File srcFolder = new File("src");
        File testFolder = new File(srcFolder, "test");
        dataFolder = new File(testFolder, "resources");
    }

    @Override
    public File getDataFolder() {
        return dataFolder;
    }

    @Override
    public InputStream getResource(String filename) {
        return new FileResourceProvider.DummyInputStream();
    }

    @Override
    public void saveResource(String filename, boolean force) {
        // Do nothing
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

}
