package dev.ratas.mobcolors;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.plugin.java.JavaPlugin;

import dev.ratas.mobcolors.commands.ParentCommand;
import dev.ratas.mobcolors.config.abstraction.PluginResourceProvider;
import dev.ratas.mobcolors.config.abstraction.ResourceProvider;
import dev.ratas.mobcolors.config.abstraction.SettingsConfigProvider;
import dev.ratas.mobcolors.config.abstraction.PluginSettingsConfigProvider;
import dev.ratas.mobcolors.config.mob.MobSettings;
import dev.ratas.mobcolors.config.mob.MobType;
import dev.ratas.mobcolors.events.ListenerRegistrator;
import dev.ratas.mobcolors.platform.PluginPlatform;
import dev.ratas.mobcolors.scheduling.abstraction.Scheduler;
import dev.ratas.mobcolors.utils.VersionProvider;
import dev.ratas.mobcolors.utils.WorldProvider;

public class MobColors extends JavaPlugin {
    private static final int BSTATS_ID = 12614;

    @Override
    public void onEnable() {
        Scheduler scheduler = new Scheduler(this, this.getServer().getScheduler());
        ResourceProvider resourceProvider = new PluginResourceProvider(this);
        SettingsConfigProvider settingsProvider = new PluginSettingsConfigProvider(this);
        ListenerRegistrator listenerRegistrator = new ListenerRegistrator(this);
        VersionProvider versionProvider = new VersionProvider(this);
        WorldProvider worldProvider = new WorldProvider(this);
        PluginProvider pluginProvider = new PluginProvider.Implementation(this);

        // platform
        PluginPlatform platform = new PluginPlatform(scheduler, resourceProvider, settingsProvider, listenerRegistrator,
                versionProvider, pluginProvider, worldProvider, () -> reloadConfig());

        // commands
        getCommand("mobcolors").setExecutor(
                new ParentCommand(platform, worldProvider, platform.getMessages(), platform.getSettings()));

        // metrics
        if (platform.getSettings().enableMetrics()) {
            Metrics metrics = new Metrics(this, BSTATS_ID);
            for (MobType type : MobType.availableValues()) {
                metrics.addCustomChart(new SimplePie(String.format("mob_%s", type.name()), () -> {
                    MobSettings ms = platform.getSettings().getSettings(type);
                    return ms == null ? "disabled" : "enabled";
                }));
            }
        }

    }

}
