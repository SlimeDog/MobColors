package dev.ratas.mobcolors.config.abstraction;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

public interface ResourceProvider {

    File getDataFolder();

    InputStream getResource(String filename);

    void saveResource(String filename, boolean force);

    Logger getLogger();

}
