package dev.ratas.mobcolors.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;

import dev.ratas.mobcolors.PluginProvider;
import dev.ratas.mobcolors.coloring.settings.ColorMap;
import dev.ratas.mobcolors.config.abstraction.SettingsConfigProvider;
import dev.ratas.mobcolors.config.mob.IllegalMobSettingsException;
import dev.ratas.mobcolors.config.mob.MobSettings;
import dev.ratas.mobcolors.config.mob.MobSettingsParser;
import dev.ratas.mobcolors.config.mob.MobType;
import dev.ratas.mobcolors.config.mob.IllegalMobSettingsException.MobTypeNotAvailableException;
import dev.ratas.mobcolors.config.world.WorldManager;
import dev.ratas.mobcolors.reload.Reloadable;
import dev.ratas.mobcolors.scheduling.abstraction.Scheduler;

public class Settings implements Reloadable {
    private final SettingsConfigProvider provider;
    private final Messages messages;
    private boolean isDebug;
    private final boolean enableAll;
    private final Scheduler scheduler;
    private final Map<MobType, MobSettings> mobSettings = new EnumMap<>(MobType.class);
    private final WorldManager worldManager = new WorldManager();
    private final Logger logger;
    private final PluginProvider pluginProvider;

    public Settings(SettingsConfigProvider provider, Messages messages, PluginProvider pluginProvider,
            Scheduler scheduler) {
        this(provider, messages, pluginProvider, scheduler, false);
    }

    public Settings(SettingsConfigProvider provider, Messages messages, PluginProvider pluginProvider,
            Scheduler scheduler, boolean enableAll) {
        this.provider = provider;
        this.messages = messages;
        this.pluginProvider = pluginProvider;
        this.scheduler = scheduler;
        this.logger = provider.getLogger();
        this.enableAll = enableAll; // for testing
        load();
    }

    private void load() {
        isDebug = _getDebug();
        worldManager.clear();
        mobSettings.clear();
        ConfigurationSection mobsSection = provider.getMobsConfig();
        if (mobsSection == null) {
            logger.info("No mobs section found in config!");
            return;
        }
        for (String mobName : mobsSection.getKeys(false)) {
            loadMob(mobName, mobsSection.getConfigurationSection(mobName));
        }
        // find all named worlds in all colormaps
        List<String> namedWorlds = new ArrayList<>();
        for (MobSettings mob : mobSettings.values()) {
            for (ColorMap<?> map : mob.getAllColorMaps()) {
                namedWorlds.addAll(map.getApplicableWorlds());
            }
        }
        // register named worlds before adding mob settings
        // this makes sure tha the defaults get added to all worlds correctly
        worldManager.setAllUsedWorlds(namedWorlds);
        for (MobSettings settings : mobSettings.values()) {
            worldManager.addMobSettings(settings, scheduler);
        }
    }

    private void loadMob(String mobName, ConfigurationSection section) {
        if (section == null) {
            logger.warning("Problem with mob settings for " + mobName + " - not a section");
            return;
        }
        MobSettingsParser parser;
        try {
            parser = new MobSettingsParser(section, enableAll);
        } catch (MobTypeNotAvailableException e) {
            logger.warning(messages.getMobTypeNotAvailable(e.getMobTypeName()));
            return;
        } catch (IllegalMobSettingsException e) {
            logger.warning("Problem with mob settings for " + mobName + ": " + e.getMessage());
            return;
        }
        if (!parser.isEnabled()) {
            return; // not enabled - not care
        }
        if (!parser.isValid()) {
            logger.warning("Problem with mob settings for " + mobName + ": invalid config");
            return;
        }
        MobSettings settings = parser.get();
        if (settings == null) {
            return; // disabled
        }
        if (settings.getEntityType() == MobType.sheep && pluginProvider.isPluginEnabled("SheepSpawnColors")) {
            logger.warning("Detected the SheepSpawnColors plugin. "
                    + "Disabling the sheep spawning functionality of MobColors "
                    + "since both plugins would otherwise attempt to do similar things.");
            return;
        }
        if (mobSettings.putIfAbsent(settings.getEntityType(), settings) != null) {
            logger.warning("Multiple mob settings found for " + mobName + ". Only using first defined one.");
        } else { // on success
            // moved this to a later stage in order to capture all the worlds that are
            // registered and thus be able to register the defaults in all possible worlds
            // worldManager.addMobSettings(settings, scheduler);
        }
    }

    public MobSettings getSettings(MobType type) {
        return mobSettings.get(type);
    }

    public List<MobSettings> getEnabledMobSettings(boolean sorted) {
        List<MobSettings> list = new ArrayList<>(mobSettings.values());
        if (sorted) {
            Collections.sort(list, (s1, s2) -> s1.getEntityType().name().compareTo(s2.getEntityType().name()));
        }
        return list;
    }

    private boolean _getDebug() {
        return provider.getBaseSettingsConfig().getBoolean("debug", false);
    }

    public boolean isOnDebug() {
        return isDebug;
    }

    @Override
    public void reload() {
        load();
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }

    public boolean checkForUpdates() {
        return provider.getBaseSettingsConfig().getBoolean("check-for-updates", true);
    }

    public boolean enableMetrics() {
        return provider.getBaseSettingsConfig().getBoolean("enable-metrics", true);
    }

    public double colorDistanceUpdateProgress() {
        return provider.getBaseSettingsConfig().getInt("report-distance-color-progress", 25) / 100.0;
    }

    public double scanDistanceUpdateProgress() {
        return provider.getBaseSettingsConfig().getInt("report-distance-scan-progress", 25) / 100.0;
    }

    public double colorRegionUpdateProgress() {
        return provider.getBaseSettingsConfig().getInt("report-region-color-progress", 10) / 100.0;
    }

    public double scanRegionUpdateProgress() {
        return provider.getBaseSettingsConfig().getInt("report-region-scan-progress", 25) / 100.0;
    }

    public long maxMsPerTickInScheduler() {
        return provider.getBaseSettingsConfig().getLong("max-ms-per-tick-in-scheduler", 20L);
    }

    public double maxDistanceForCommands() {
        return provider.getBaseSettingsConfig().getDouble("distance-limit", 512.0D);
    }

}