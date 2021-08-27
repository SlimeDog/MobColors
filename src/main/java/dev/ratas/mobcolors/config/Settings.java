package dev.ratas.mobcolors.config;

import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import dev.ratas.mobcolors.config.abstraction.SettingsConfigProvider;
import dev.ratas.mobcolors.config.mob.IllegalMobSettingsException;
import dev.ratas.mobcolors.config.mob.MobSettings;
import dev.ratas.mobcolors.config.mob.MobSettingsParser;
import dev.ratas.mobcolors.config.world.WorldManager;
import dev.ratas.mobcolors.reload.Reloadable;
import dev.ratas.mobcolors.scheduling.abstraction.Scheduler;

public class Settings implements Reloadable {
    private final SettingsConfigProvider provider;
    private boolean isDebug;
    private final Scheduler scheduler;
    private final Map<EntityType, MobSettings> mobSettings = new EnumMap<>(EntityType.class);
    private final WorldManager worldManager = new WorldManager();
    private final Logger logger;

    public Settings(SettingsConfigProvider provider, Scheduler scheduler) {
        this.provider = provider;
        this.scheduler = scheduler;
        this.logger = provider.getLogger();
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
    }

    private void loadMob(String mobName, ConfigurationSection section) {
        if (section == null) {
            logger.warning("Problem with mob settings for " + mobName + " - not a section");
            return;
        }
        MobSettingsParser parser;
        try {
            parser = new MobSettingsParser(section, logger);
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
        if (settings.getEntityType() == EntityType.SHEEP
                && Bukkit.getPluginManager().isPluginEnabled("SheepSpawnColors")) {
            logger.warning("Detected the SheepSpawnColors plugin. "
                    + "Disabling the sheep spawning functionality of MobColors "
                    + "since both plugins would otherwise attempt to do similar things.");
            return;
        }
        if (mobSettings.putIfAbsent(settings.getEntityType(), settings) != null) {
            logger.warning("Multiple mob settings found for " + mobName + ". Only using first defined one.");
        } else { // on success
            worldManager.addMobSettings(settings, scheduler);
        }
    }

    public MobSettings getSettings(EntityType type) {
        return mobSettings.get(type);
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

    public long ticksBetweenLongTaskUpdates() {
        return provider.getBaseSettingsConfig().getLong("ticks-between-task-update", 40L);
    }

    public long maxMsPerTickInScheduler() {
        return provider.getBaseSettingsConfig().getLong("max-ms-per-tick-in-scheduler", 20L);
    }

}