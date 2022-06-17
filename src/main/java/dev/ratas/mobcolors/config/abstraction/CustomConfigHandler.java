package dev.ratas.mobcolors.config.abstraction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import dev.ratas.mobcolors.reload.ReloadException;
import dev.ratas.mobcolors.reload.Reloadable;

/**
 * CustomConfig
 */
public class CustomConfigHandler implements Reloadable {
    private final ResourceProvider provider;
    private final String fileName;
    private final boolean noSave;
    private FileConfiguration customConfig = null;
    private File customConfigFile = null;

    public CustomConfigHandler(ResourceProvider provider, String name) throws InvalidConfigurationException {
        this(provider, name, false);
    }

    public CustomConfigHandler(ResourceProvider provider, String name, boolean noSave)
            throws InvalidConfigurationException {
        this.provider = provider;
        this.fileName = name;
        this.noSave = noSave;
        saveDefaultConfig();
        reloadConfig();
    }

    public boolean reloadConfig() throws InvalidConfigurationException {
        if (customConfigFile == null) {
            customConfigFile = new File(provider.getDataFolder(), fileName);
        }
        customConfig = loadConfiguration(customConfigFile);

        // Look for defaults in the jar
        Reader defConfigStream = null;
        InputStream resource = provider.getResource(fileName);
        if (resource != null) {
            try {
                defConfigStream = new InputStreamReader(resource, "UTF8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            customConfig.setDefaults(defConfig);
        }
        if (customConfig.getKeys(true).isEmpty()) {
            if (!noSave)
                saveConfig();
            return false;
        } else {
            return true;
        }
    }

    public File getFile() {
        return customConfigFile;
    }

    public FileConfiguration getConfig() {
        return customConfig;
    }

    public void saveConfig() {
        if (customConfig == null || customConfigFile == null) {
            return;
        }
        try {
            getConfig().save(customConfigFile);
        } catch (IOException ex) {
            provider.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
        }
    }

    public void saveDefaultConfig() {
        if (customConfigFile == null) {
            customConfigFile = new File(provider.getDataFolder(), fileName);
        }
        if (!customConfigFile.exists() && provider.getResource(fileName) != null) {
            provider.saveResource(fileName, false);
        }
    }

    public static YamlConfiguration loadConfiguration(File file) throws InvalidConfigurationException {
        Validate.notNull(file, "File cannot be null");

        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (FileNotFoundException ex) {
            // empty
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
        }
        return config;
    }

    @Override
    public void reload() throws ReloadException {
        try {
            reloadConfig();
        } catch (InvalidConfigurationException e) {
            throw new ReloadException(this, e.getMessage());
        }
    }

}