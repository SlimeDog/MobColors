package dev.ratas.mobcolors;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;

import dev.ratas.mobcolors.commands.ParentCommand;
import dev.ratas.mobcolors.config.mob.MobSettings;
import dev.ratas.mobcolors.config.mob.MobType;
import dev.ratas.mobcolors.platform.PluginPlatform;
import dev.ratas.slimedogcore.impl.SlimeDogCore;

public class MobColors extends SlimeDogCore {
    private static final int BSTATS_ID = 12614;

    @Override
    public void pluginEnabled() {
        PluginProvider pluginProvider = new PluginProvider.Implementation(this);

        // platform
        PluginPlatform platform = new PluginPlatform(this, pluginProvider);

        // commands
        getCommand("mobcolors").setExecutor(
                new ParentCommand(platform, getWorldProvider(), platform.getMessages(), platform.getSettings()));

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

    @Override
    public void pluginDisabled() {

    }

}
