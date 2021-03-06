package dev.ratas.mobcolors.commands.subcommands;

import java.util.Collections;
import java.util.List;

import dev.ratas.mobcolors.config.Messages;
import dev.ratas.mobcolors.platform.PluginPlatform;
import dev.ratas.slimedogcore.api.commands.SDCCommandOptionSet;
import dev.ratas.slimedogcore.api.messaging.recipient.SDCRecipient;
import dev.ratas.slimedogcore.impl.commands.AbstractSubCommand;

public class ReloadSubCommand extends AbstractSubCommand {
    private static final String NAME = "reload";
    private static final String USAGE = "/mobcolors reload";
    private static final String PERMS = "mobcolors.reload";
    private final PluginPlatform platform;
    private final Messages messages;

    public ReloadSubCommand(PluginPlatform platform, Messages messages) {
        super(NAME, PERMS, USAGE, true, false);
        this.platform = platform;
        this.messages = messages;
    }

    @Override
    public List<String> onTabComplete(SDCRecipient sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public boolean onOptionedCommand(SDCRecipient sender, String[] args, SDCCommandOptionSet options) {
        if (platform.reload()) {
            sender.sendMessage(messages.getReloadedMessage().getMessage());
        } else {
            sender.sendMessage(messages.getProblemReloadingMessage().getMessage());
        }
        return true;
    }

}
