package dev.ratas.mobcolors.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

public interface SubCommand {

    public String getName();

    public String getUsage(CommandSender sender, String[] args);

    public boolean hasPermission(CommandSender sender);

    public List<String> getTabComletions(CommandSender sender, String[] args);

    public boolean executeCommand(CommandSender sender, String[] args);

    public boolean showTabCompletion();

    public boolean needsPlayer();

}
