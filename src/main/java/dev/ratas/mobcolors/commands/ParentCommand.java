package dev.ratas.mobcolors.commands;

import dev.ratas.mobcolors.commands.subcommands.ColorSubCommand;
import dev.ratas.mobcolors.commands.subcommands.InfoSubCommand;
import dev.ratas.mobcolors.commands.subcommands.ReloadSubCommand;
import dev.ratas.mobcolors.commands.subcommands.ScanSubCommand;
import dev.ratas.mobcolors.commands.subcommands.SchedulerInfoSubCommand;
import dev.ratas.mobcolors.config.Messages;
import dev.ratas.mobcolors.config.Settings;
import dev.ratas.mobcolors.platform.PluginPlatform;
import dev.ratas.mobcolors.utils.WorldProvider;

public class ParentCommand extends AbstractParentCommand {

    public ParentCommand(PluginPlatform platform, WorldProvider worldProvider, Messages messages, Settings settings) {
        addSubCommand(new ReloadSubCommand(platform, messages));
        addSubCommand(new ColorSubCommand(platform.getMapper(), worldProvider, settings, messages));
        addSubCommand(new ScanSubCommand(platform.getScanner(), settings, messages));
        addSubCommand(new SchedulerInfoSubCommand(platform.getScheduler(), settings.isOnDebug()));
        addSubCommand(new InfoSubCommand(settings, worldProvider, messages));
    }

}