package dev.ratas.mobcolors.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import dev.ratas.mobcolors.config.abstraction.ResourceProvider;

public class FileResourceProvider implements ResourceProvider {
    private final Logger logger;
    private final File dataFolder;

    FileResourceProvider(Logger logger) {
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
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        }
        File file = new File(getDataFolder(), filename);
        if (!file.exists()) {
            return new DummyInputStream();
        }
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveResource(String filename, boolean force) {
        logger.warning("saveResource not supported");
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    static class DummyInputStream extends InputStream {

        @Override
        public int read() throws IOException {
            return -1; // end of stream
        }

    }

}
