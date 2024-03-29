package dev.ratas.mobcolors.platform;

import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import dev.ratas.mobcolors.PluginProvider;
import dev.ratas.mobcolors.SpawnListener;
import dev.ratas.mobcolors.config.Messages;
import dev.ratas.mobcolors.config.Settings;
import dev.ratas.mobcolors.region.RegionMapper;
import dev.ratas.mobcolors.region.RegionScanner;
import dev.ratas.mobcolors.scheduling.SimpleTaskScheduler;
import dev.ratas.mobcolors.scheduling.TaskScheduler;
import dev.ratas.mobcolors.utils.LogUtils;
import dev.ratas.slimedogcore.api.SlimeDogPlugin;
import dev.ratas.slimedogcore.api.config.SDCCustomConfig;
import dev.ratas.slimedogcore.api.reload.ReloadException;
import dev.ratas.slimedogcore.impl.utils.UpdateChecker;

public class PluginPlatform {
    private static final int SPIGOT_RESOURCE_ID = 96771;
    private static final String HANGAR_AUTHOR = "SlimeDog";
    private static final String HANGAR_SLUG = "MobColors";
    private final SDCCustomConfig config;
    private final Settings settings;
    private final Messages messages;
    private final SpawnListener spawnListener;
    private final TaskScheduler taskScheduler;
    private final RegionMapper mapper;
    private final RegionScanner scanner;
    private final Logger logger;
    private final SlimeDogPlugin plugin;

    public PluginPlatform(SlimeDogPlugin plugin, PluginProvider pluginProvider, BooleanSupplier mainThread)
            throws PlatformInitializationException {
        this.logger = LogUtils.getLogger();
        this.plugin = plugin;
        // initialize and register reloadables
        try {
            messages = new Messages(plugin);
        } catch (Exception e) {
            disableWith(e);
            throw new PlatformInitializationException("Messages issue");
        }
        plugin.getReloadManager().register(messages);
        try {
            config = plugin.getDefaultConfig();
        } catch (Exception e) {
            disableWith(e);
            throw new PlatformInitializationException("Config issue");
        }
        try {
            settings = new Settings(plugin, plugin.getCustomConfigManager(), messages, plugin.getPluginManager(),
                    plugin.getScheduler());
        } catch (Exception e) {
            disableWith(e);
            throw new PlatformInitializationException("Settings issue");
        }
        plugin.getReloadManager().register(settings);
        spawnListener = new SpawnListener(settings, plugin.getWorldProvider(), plugin.getScheduler(), mainThread);
        // scheduling, scanning, mapping
        this.taskScheduler = new SimpleTaskScheduler(settings.maxMsPerTickInScheduler());
        plugin.getScheduler().runTaskTimer((Runnable) this.taskScheduler, 1L, 1L);
        scanner = new RegionScanner(plugin.getScheduler(), this.taskScheduler, plugin.getPluginManager(),
                plugin.getWorldProvider(), mainThread);
        mapper = new RegionMapper(plugin.getScheduler(), this.taskScheduler, spawnListener, scanner,
                plugin.getPluginManager(), plugin.getWorldProvider(), mainThread);

        plugin.getPluginManager().registerEvents(spawnListener);

        if (config.getConfig().isSet("debug")) {
            boolean debug = settings.isOnDebug();
            logger.info("Debug " + (debug ? "enabled" : "disabled"));
        }

        // update
        if (settings.checkForUpdates()) {
            String source = plugin.getDefaultConfig().getConfig().getString("update-source", "Hangar");
            BiConsumer<UpdateChecker.VersionResponse, String> consumer = (response, version) -> {
                switch (response) {
                    case LATEST:
                        logger.info(messages.updateCurrentVersion().getMessage().getFilled());
                        break;
                    case FOUND_NEW:
                        logger.info(messages.updateNewVersionAvailable().createWith(version).getFilled());
                        break;
                    case UNAVAILABLE:
                        logger.info(messages.updateInfoUnavailable().getMessage().getFilled());
                        break;
                }
            };
            UpdateChecker checker;
            if (source.equalsIgnoreCase("Hangar")) {
                checker = UpdateChecker.forHangar(plugin, consumer, HANGAR_AUTHOR, HANGAR_SLUG);
            } else {
                checker = UpdateChecker.forSpigot(plugin, consumer, SPIGOT_RESOURCE_ID);
            }
            checker.check();
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
        try {
            plugin.getReloadManager().reload();
        } catch (ReloadException e) {
            logger.severe("Problem reloading plugin:");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Settings getSettings() {
        return settings;
    }

    public Messages getMessages() {
        return messages;
    }

    public void debug(String msg) {
        if (!settings.isOnDebug())
            return;
        logger.info("DEBUG " + msg);
    }

}
