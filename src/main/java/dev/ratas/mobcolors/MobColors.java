package dev.ratas.mobcolors;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import dev.ratas.mobcolors.commands.ParentCommand;
import dev.ratas.mobcolors.config.abstraction.ResourceProvider;
import dev.ratas.mobcolors.config.abstraction.SettingsConfigProvider;
import dev.ratas.mobcolors.events.ListenerRegistrator;
import dev.ratas.mobcolors.platform.PluginPlatform;
import dev.ratas.mobcolors.scheduling.abstraction.Scheduler;
import dev.ratas.mobcolors.utils.VersionProvider;
import dev.ratas.mobcolors.utils.WorldProvider;

public class MobColors extends JavaPlugin {
    private static final int ID = -1; // TODO - do ID

    @Override
    public void onEnable() {
        Scheduler scheduler = new Scheduler(this, this.getServer().getScheduler());
        ResourceProvider resourceProvider = new ResourceProvider(this);
        SettingsConfigProvider settingsProvider = new SettingsConfigProvider(this);
        ListenerRegistrator listenerRegistrator = new ListenerRegistrator(this);
        VersionProvider versionProvider = new VersionProvider(this);
        WorldProvider worldProvider = new WorldProvider(this);

        // platform
        PluginPlatform platform = new PluginPlatform(scheduler, resourceProvider, settingsProvider, listenerRegistrator,
                versionProvider, () -> reloadConfig(), getLogger());

        // commands
        getCommand("mobcolors").setExecutor(
                new ParentCommand(platform, worldProvider, platform.getMessages(), platform.getSettings()));

        // metrics
        if (platform.getSettings().enableMetrics()) { // TODO - enable with ID
            new Metrics(this, ID);
        }

    }

}
