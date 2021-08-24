package dev.ratas.mobcolors.commands.subcommands;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import dev.ratas.mobcolors.commands.SimpleSubCommand;
import dev.ratas.mobcolors.config.Messages;
import dev.ratas.mobcolors.platform.PluginPlatform;

public class ReloadSubCommand extends SimpleSubCommand {
    private static final String NAME = "reload";
    private static final String USAGE = "/mobcolors reload";
    private static final String PERMS = "mobcolors.reload";
    private final PluginPlatform platform;
    private final Messages messages;

    public ReloadSubCommand(PluginPlatform platform, Messages messages) {
        super(NAME, USAGE, PERMS, false);
        this.platform = platform;
        this.messages = messages;
    }

    @Override
    public List<String> getTabComletions(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public boolean executeCommand(CommandSender sender, String[] args) {
        if (platform.reload()) {
            sender.sendMessage(messages.getReloadedMessage());
        } else {
            sender.sendMessage(messages.getProblemReloadingMessage());
        }
        return true;
    }

}
