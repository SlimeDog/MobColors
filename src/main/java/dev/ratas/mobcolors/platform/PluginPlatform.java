package dev.ratas.mobcolors.platform;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import dev.ratas.mobcolors.SpawnListener;
import dev.ratas.mobcolors.config.Messages;
import dev.ratas.mobcolors.config.Settings;
import dev.ratas.mobcolors.config.abstraction.CustomConfigHandler;
import dev.ratas.mobcolors.config.abstraction.ResourceProvider;
import dev.ratas.mobcolors.config.abstraction.SettingsConfigProvider;
import dev.ratas.mobcolors.events.ListenerRegistrator;
import dev.ratas.mobcolors.region.RegionMapper;
import dev.ratas.mobcolors.region.RegionScanner;
import dev.ratas.mobcolors.reload.ReloadManager;
import dev.ratas.mobcolors.scheduling.SimpleTaskScheduler;
import dev.ratas.mobcolors.scheduling.TaskScheduler;
import dev.ratas.mobcolors.scheduling.abstraction.Scheduler;
import dev.ratas.mobcolors.utils.UpdateChecker;
import dev.ratas.mobcolors.utils.VersionProvider;

public class PluginPlatform {
    private final ReloadManager reloadManager = new ReloadManager();
    private final CustomConfigHandler config;
    private final Settings settings;
    private final Messages messages;
    private final SpawnListener spawnListener;
    private final TaskScheduler taskScheduler;
    private final RegionMapper mapper;
    private final RegionScanner scanner;
    private final Logger logger;

    public PluginPlatform(Scheduler scheduler, ResourceProvider resourceProvider,
            SettingsConfigProvider settingsProvider, ListenerRegistrator lisenerRegistrator,
            VersionProvider versionProvider, Logger logger) throws PlatformInitializationException {
        this.logger = logger;
        // initialize and register reloadables
        try {
            config = new CustomConfigHandler(resourceProvider, "config.yml");
        } catch (Exception e) {
            disableWith(e);
            throw new PlatformInitializationException("Config issue");
        }
        reloadManager.register(config);
        try {
            settings = new Settings(settingsProvider, scheduler);
        } catch (Exception e) {
            disableWith(e);
            throw new PlatformInitializationException("Settings issue");
        }
        reloadManager.register(settings);
        try {
            messages = new Messages(resourceProvider);
        } catch (Exception e) {
            disableWith(e);
            throw new PlatformInitializationException("Messages issue");
        }
        reloadManager.register(messages);
        spawnListener = new SpawnListener(settings);
        // scheduling, scanning, mapping
        this.taskScheduler = new SimpleTaskScheduler(settings.maxMsPerTickInScheduler());
        scheduler.scheduleRepeating((Runnable) this.taskScheduler, 1L, 1L);
        mapper = new RegionMapper(this.taskScheduler, spawnListener);
        scanner = new RegionScanner(this.taskScheduler);

        lisenerRegistrator.register(spawnListener);

        if (config.getConfig().isSet("debug")) {
            boolean debug = settings.isOnDebug();
            logger.info("Debug " + (debug ? "enabled" : "disabled"));
        }

        // update
        if (settings.checkForUpdates() && settings.checkForUpdates()) {
            new UpdateChecker(scheduler, versionProvider.getVersion(), (response, version) -> {
                switch (response) {
                    case LATEST:
                        logger.info(messages.updateCurrentVersion());
                        break;
                    case FOUND_NEW:
                        logger.info(messages.updateNewVersionAvailable(version));
                        break;
                    case UNAVAILABLE:
                        logger.info(messages.updateInfoUnavailable());
                        break;
                }
            }).check();
        }
    }

    public SpawnListener getSpawnListener() {
        return spawnListener;
    }

    public TaskScheduler getScheduler() {
        return taskScheduler;
    }

    public RegionMapper getMapper() {
        return mapper;
    }

    public RegionScanner getScanner() {
        return scanner;
    }

    public void disableWith(Throwable e) {
        logger.severe("Problem loading config:" + e.getMessage());
        logger.severe("Disabling plugin!");
        // allowing Bukkit access since everything is broken by now anyway
        Bukkit.getServer().getPluginManager().disablePlugin(JavaPlugin.getProvidingPlugin(getClass()));
    }

    public boolean reload() {
        boolean successful = reloadManager.reload(this);
        if (config.getConfig().isSet("debug")) {
            boolean debug = settings.isOnDebug();
            logger.info("Debug " + (debug ? "enabled" : "disabled"));
        }
        return successful;
    }

    public Settings getSettings() {
        return settings;
    }

    public Messages getMessages() {
        return messages;
    }

    public FileConfiguration getConfig() {
        return config.getConfig();
    }

    public void debug(String msg) {
        if (!settings.isOnDebug())
            return;
        logger.info("DEBUG " + msg);
    }

    public ReloadManager getReloadManager() {
        return reloadManager;
    }

}
