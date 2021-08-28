package dev.ratas.mobcolors.commands.subcommands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.util.StringUtil;

import dev.ratas.mobcolors.coloring.settings.ColorMap;
import dev.ratas.mobcolors.commands.SimpleSubCommand;
import dev.ratas.mobcolors.config.Messages;
import dev.ratas.mobcolors.config.Settings;
import dev.ratas.mobcolors.config.mob.MobSettings;
import dev.ratas.mobcolors.config.world.WorldSettings;
import dev.ratas.mobcolors.utils.WorldProvider;

public class InfoSubCommand extends SimpleSubCommand {
    private static final String NAME = "info";
    private static final String USAGE = "/mobcolors info [world]";
    private static final String PERMS = "mobcolors.info";
    private final Settings settings;
    private final WorldProvider worldProvider;
    private final Messages messages;

    public InfoSubCommand(Settings settings, WorldProvider worldProvider, Messages messages) {
        super(NAME, USAGE, PERMS, false, true);
        this.settings = settings;
        this.worldProvider = worldProvider;
        this.messages = messages;
    }

    @Override
    public List<String> getTabComletions(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], worldProvider.getWorldNames(), list);
        }
        return list;
    }

    @Override
    public boolean executeCommand(CommandSender sender, String[] args) {
        if (args.length < 1) {
            showEnabledColorMaps(sender);
        } else {
            World world = worldProvider.getWorld(args[0]);
            if (world == null) {
                sender.sendMessage(messages.getWorldNotFoundMessage(args[0]));
            } else {
                showColorMapsInWorld(world, sender);
            }
        }
        return true;
    }

    private void showEnabledColorMaps(CommandSender sender) {
        boolean foundEnabledColorMap = false;
        for (MobSettings mobSettings : settings.getEnabledMobSettings()) {
            List<EnabledColorMapInfo> enabledMaps = getEnabledColorMaps(mobSettings);
            if (enabledMaps.isEmpty()) {
                continue;
            }
            EntityType type = mobSettings.getEntityType();
            sender.sendMessage(messages.getEnabledMobColorMapsHeaderMessage(type));
            for (EnabledColorMapInfo info : enabledMaps) {
                sender.sendMessage(messages.getEnabledMobColorMapItemMessage(info.map.getName(), info.activeWorlds));
            }
            foundEnabledColorMap = true;
            String defaultsMessage;
            if (mobSettings.getDefaultColorMap() != null) {
                defaultsMessage = messages.getMobColorMapDefaultEnabledMessage();
            } else {
                defaultsMessage = messages.getMobColorMapDefaultDisabledMessage();
            }
            sender.sendMessage(defaultsMessage);
        }
        if (!foundEnabledColorMap) {
            sender.sendMessage(messages.getNoColormapsEnabledMessage());
        }
    }

    private List<EnabledColorMapInfo> getEnabledColorMaps(MobSettings settings) {
        List<EnabledColorMapInfo> maps = new ArrayList<>();
        for (ColorMap<?> map : settings.getAllColorMaps()) {
            List<String> activeWorlds = getActiveWorlds(map.getApplicableWorlds());
            if (!activeWorlds.isEmpty()) {
                maps.add(new EnabledColorMapInfo(map, activeWorlds));
            }
        }
        return maps;
    }

    private List<String> getActiveWorlds(Collection<String> worlds) {
        List<String> activeWorlds = new ArrayList<>();
        for (String worldName : worlds) {
            if (worldProvider.getWorld(worldName) != null) {
                activeWorlds.add(worldName);
            }
        }
        return activeWorlds;
    }

    private void showColorMapsInWorld(World world, CommandSender sender) {
        WorldSettings worldSettings = settings.getWorldManager().getWorldSettings(world);
        if (worldSettings == null) {
            sender.sendMessage(messages.getNoColorMapsInWorldMessage(world));
            return;
        }
        sender.sendMessage(messages.getWorldColorMapsHeaderMessage(world));
        for (ColorMap<?> map : worldSettings.getEnabledColorMaps()) {
            sender.sendMessage(messages.getWorldColorMapItemMessage(map.getApplicableEntityType(), map.getName()));
        }
    }

    private class EnabledColorMapInfo {
        private final ColorMap<?> map;
        private final List<String> activeWorlds;

        private EnabledColorMapInfo(ColorMap<?> map, List<String> activeWorlds) {
            this.map = map;
            this.activeWorlds = activeWorlds;
        }
    }

}
