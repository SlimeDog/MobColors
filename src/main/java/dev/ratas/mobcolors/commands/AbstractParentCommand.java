package dev.ratas.mobcolors.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import dev.ratas.mobcolors.utils.StringIgnoreCase;

public abstract class AbstractParentCommand implements TabExecutor {
    private final Map<StringIgnoreCase, SubCommand> commands = new LinkedHashMap<>();

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            List<String> subs = new ArrayList<>();
            for (SubCommand sc : commands.values()) {
                if (sc.showTabCompletion() && sc.hasPermission(sender)) {
                    subs.add(sc.getName());
                }
            }
            return StringUtil.copyPartialMatches(args[0], subs, list);
        }
        SubCommand sub = getSubCommand(args[0]);
        if (sub == null || !sub.hasPermission(sender))
            return list;
        return sub.getTabComletions(sender, Arrays.copyOfRange(args, 1, args.length));
    }

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(getUsage(sender, args));
            return true;
        }
        SubCommand sub = getSubCommand(args[0]);
        if (sub == null || !sub.hasPermission(sender)) {
            sender.sendMessage(getUsage(sender, args));
            return true;
        }
        if (sub.needsPlayer() && !(sender instanceof Player)) {
            sender.sendMessage("This command can only be used in game!");
            return true;
        }
        if (!sub.executeCommand(sender, Arrays.copyOfRange(args, 1, args.length))) {
            sender.sendMessage(sub.getUsage(sender, args));
        }
        return true;
    }

    public void addSubCommand(SubCommand cmd) {
        commands.put(new StringIgnoreCase(cmd.getName()), cmd);
    }

    public SubCommand removeSubCommand(String name) {
        return commands.remove(new StringIgnoreCase(name));
    }

    public SubCommand getSubCommand(String name) {
        return commands.get(new StringIgnoreCase(name));
    }

    public List<SubCommand> getSubCommands() {
        return new ArrayList<>(commands.values());
    }

    public String getUsage(CommandSender sender, String[] args) {
        StringBuilder builder = new StringBuilder();
        for (SubCommand sub : getSubCommands()) {
            if (sub.hasPermission(sender) && sub.showTabCompletion()) {
                if (builder.length() > 0) {
                    builder.append("\n");
                }
                builder.append(sub.getUsage(sender, args));
            }
        }
        return builder.toString();
    }

}
