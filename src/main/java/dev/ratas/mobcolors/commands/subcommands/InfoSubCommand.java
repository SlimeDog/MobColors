package dev.ratas.mobcolors.commands.subcommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import dev.ratas.mobcolors.coloring.settings.ColorMap;
import dev.ratas.mobcolors.commands.SimpleSubCommand;
import dev.ratas.mobcolors.config.Messages;
import dev.ratas.mobcolors.config.Settings;
import dev.ratas.mobcolors.config.mob.MobSettings;
import dev.ratas.mobcolors.config.mob.MobType;
import dev.ratas.mobcolors.config.mob.MobTypes;
import dev.ratas.mobcolors.config.world.WorldSettings;
import dev.ratas.mobcolors.utils.WorldDescriptor;
import dev.ratas.mobcolors.utils.WorldProvider;

public class InfoSubCommand extends SimpleSubCommand {
    private static final String NAME = "info";
    private static final String USAGE = "/mobcolors info [world] [ --mob <mob-type> ]";
    private static final String PERMS = "mobcolors.info";
    private static final List<String> OPTIONS = Collections.unmodifiableList(Arrays.asList("--mob"));
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
            if (args[0].startsWith("--")) {
                return StringUtil.copyPartialMatches(args[0], OPTIONS, list);
            }
            return StringUtil.copyPartialMatches(args[0], worldProvider.getWorldNames(), list);
        } else if (args.length == 2) {
            return StringUtil.copyPartialMatches(args[1], OPTIONS, list);
        } else if (args.length == 3 && args[1].equals("--mob")) {
            return StringUtil.copyPartialMatches(args[args.length - 1], MobTypes.ENTITY_TYPE_NAMES, list);
        }
        return list;
    }

    @Override
    public boolean executeCommand(CommandSender sender, String[] args) {
        MobType targetType = getTargetType(args);
        if (args.length < 1 || args[0].equalsIgnoreCase("--mob")) {
            showEnabledColorMaps(sender, targetType);
        } else {
            World world = worldProvider.getWorld(args[0]);
            if (world == null) {
                sender.sendMessage(messages.getWorldNotFoundMessage(args[0]));
            } else {
                showColorMapsInWorld(WorldDescriptor.wrap(world), sender, targetType);
            }
        }
        return true;
    }

    private void showEnabledColorMaps(CommandSender sender, MobType targetType) {
        boolean foundEnabledColorMap = false;
        for (MobSettings mobSettings : settings.getEnabledMobSettings(true)) {
            List<EnabledColorMapInfo> enabledMaps = getEnabledColorMaps(mobSettings);
            MobType type = mobSettings.getEntityType();
            if (targetType != null && type != targetType) {
                continue;
            }
            sender.sendMessage(messages.getEnabledMobColorMapsHeaderMessage(type));
            if (enabledMaps.isEmpty()) {
                foundEnabledColorMap = true;
                sender.sendMessage(messages.getMobColorMapDefaultEnabledEverywherMessage());
                continue;
            }
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
        Collections.sort(maps, (m1, m2) -> m1.map.getName().compareTo(m2.map.getName()));
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

    private void showColorMapsInWorld(WorldDescriptor world, CommandSender sender, MobType targetType) {
        WorldSettings worldSettings = settings.getWorldManager().getWorldSettings(world);
        if (worldSettings == null) {
            sender.sendMessage(messages.getNoColorMapsInWorldMessage(world));
            return;
        }
        sender.sendMessage(messages.getWorldColorMapsHeaderMessage(world));
        for (ColorMap<?> map : worldSettings.getEnabledColorMaps(true)) {
            MobType type = map.getApplicableEntityType();
            if (targetType != null && type != targetType) {
                continue;
            }
            sender.sendMessage(messages.getWorldColorMapItemMessage(type, map.getName()));
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
