package dev.ratas.mobcolors.commands;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

public abstract class SimpleSubCommand implements SubCommand {
    private final String name;
    private final String usage;
    private final String perms;
    private final boolean needsPlayer;
    private final boolean showOnTabComplete;

    public SimpleSubCommand(String name, String usage, String perms) {
        this(name, usage, perms, false);
    }

    public SimpleSubCommand(String name, String usage, String perms, boolean needsPlayer) {
        this(name, usage, perms, needsPlayer, true);
    }

    public SimpleSubCommand(String name, String usage, String perms, boolean needsPlayer, boolean showOnTabComplete) {
        this.name = name;
        this.usage = usage;
        this.perms = perms;
        this.needsPlayer = needsPlayer;
        this.showOnTabComplete = showOnTabComplete;
    }

    protected Set<String> getOptions(String[] args) {
        return Arrays.stream(args).filter((arg) -> arg.startsWith("--")).collect(Collectors.toSet());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUsage(CommandSender sender, String[] args) {
        return usage;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(perms);
    }

    @Override
    public boolean showTabCompletion() {
        return showOnTabComplete;
    }

    @Override
    public boolean needsPlayer() {
        return needsPlayer;
    }

}
