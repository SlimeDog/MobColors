package dev.ratas.mobcolors.commands.subcommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.World;
import org.bukkit.util.StringUtil;

import dev.ratas.mobcolors.coloring.settings.ColorMap;
import dev.ratas.mobcolors.config.Messages;
import dev.ratas.mobcolors.config.Settings;
import dev.ratas.mobcolors.config.mob.MobSettings;
import dev.ratas.mobcolors.config.mob.MobType;
import dev.ratas.mobcolors.config.mob.MobTypes;
import dev.ratas.mobcolors.config.world.WorldSettings;
import dev.ratas.mobcolors.utils.WorldDescriptor;
import dev.ratas.mobcolors.utils.CommandUtils;
import dev.ratas.slimedogcore.api.commands.SDCCommandOptionSet;
import dev.ratas.slimedogcore.api.messaging.factory.SDCDoubleContextMessageFactory;
import dev.ratas.slimedogcore.api.messaging.factory.SDCSingleContextMessageFactory;
import dev.ratas.slimedogcore.api.messaging.factory.SDCVoidContextMessageFactory;
import dev.ratas.slimedogcore.api.messaging.recipient.SDCRecipient;
import dev.ratas.slimedogcore.api.wrappers.SDCWorldProvider;
import dev.ratas.slimedogcore.impl.commands.AbstractSubCommand;

public class InfoSubCommand extends AbstractSubCommand {
    private static final String NAME = "info";
    private static final String USAGE = "/mobcolors info [world] [ --mob <mob-type> ]";
    private static final String PERMS = "mobcolors.info";
    private static final List<String> OPTIONS = Collections.unmodifiableList(Arrays.asList("--mob"));
    private final Settings settings;
    private final SDCWorldProvider worldProvider;
    private final Messages messages;

    public InfoSubCommand(Settings settings, SDCWorldProvider worldProvider, Messages messages) {
        super(NAME, PERMS, USAGE, true, false);
        this.settings = settings;
        this.worldProvider = worldProvider;
        this.messages = messages;
    }

    @Override
    public List<String> onTabComplete(SDCRecipient sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            if (args[0].startsWith("--")) {
                return StringUtil.copyPartialMatches(args[0], OPTIONS, list);
            }
            return StringUtil.copyPartialMatches(args[0], worldProvider.getAllWorldNames(), list);
        } else if (args.length == 2) {
            return StringUtil.copyPartialMatches(args[1], OPTIONS, list);
        } else if (args.length == 3 && args[1].equals("--mob")) {
            return StringUtil.copyPartialMatches(args[args.length - 1], MobTypes.ENTITY_TYPE_NAMES, list);
        }
        return list;
    }

    @Override
    public boolean onOptionedCommand(SDCRecipient sender, String[] args, SDCCommandOptionSet options) {
        MobType targetType = CommandUtils.getTargetType(args);
        if (args.length < 1 || args[0].equalsIgnoreCase("--mob")) {
            showEnabledColorMaps(sender, targetType);
        } else {
            World world = worldProvider.getWorldByName(args[0]);
            if (world == null) {
                SDCSingleContextMessageFactory<String> msg = messages.getWorldNotFoundMessage();
                sender.sendMessage(msg.getMessage(msg.getContextFactory().getContext(args[0])));
            } else {
                showColorMapsInWorld(WorldDescriptor.wrap(world), sender, targetType);
            }
        }
        return true;
    }

    private void showEnabledColorMaps(SDCRecipient sender, MobType targetType) {
        boolean foundEnabledColorMap = false;
        for (MobSettings mobSettings : settings.getEnabledMobSettings(true)) {
            List<EnabledColorMapInfo> enabledMaps = getEnabledColorMaps(mobSettings);
            MobType type = mobSettings.getEntityType();
            if (targetType != null && type != targetType) {
                continue;
            }
            SDCSingleContextMessageFactory<MobType> msg = messages.getEnabledMobColorMapsHeaderMessage();
            sender.sendMessage(msg.getMessage(msg.getContextFactory().getContext(type)));
            if (enabledMaps.isEmpty()) {
                foundEnabledColorMap = true;
                sender.sendMessage(messages.getMobColorMapDefaultEnabledEverywherMessage().getMessage());
                continue;
            }
            SDCDoubleContextMessageFactory<String, List<String>> m = messages.getEnabledMobColorMapItemMessage();
            for (EnabledColorMapInfo info : enabledMaps) {
                sender.sendMessage(
                        m.getMessage(m.getContextFactory().getContext(info.map.getName(), info.activeWorlds)));
            }
            foundEnabledColorMap = true;
            SDCVoidContextMessageFactory defaultsMessage;
            if (mobSettings.getDefaultColorMap() != null) {
                defaultsMessage = messages.getMobColorMapDefaultEnabledMessage();
            } else {
                defaultsMessage = messages.getMobColorMapDefaultDisabledMessage();
            }
            sender.sendMessage(defaultsMessage.getMessage());
        }
        if (!foundEnabledColorMap) {
            sender.sendMessage(messages.getNoColormapsEnabledMessage().getMessage());
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
            if (worldProvider.getWorldByName(worldName) != null) {
                activeWorlds.add(worldName);
            }
        }
        return activeWorlds;
    }

    private void showColorMapsInWorld(WorldDescriptor world, SDCRecipient sender, MobType targetType) {
        WorldSettings worldSettings = settings.getWorldManager().getWorldSettings(world);
        if (worldSettings == null) {
            SDCSingleContextMessageFactory<WorldDescriptor> msg = messages.getNoColorMapsInWorldMessage();
            sender.sendMessage(msg.getMessage(msg.getContextFactory().getContext(world)));
            return;
        }
        SDCSingleContextMessageFactory<WorldDescriptor> msg = messages.getWorldColorMapsHeaderMessage();
        sender.sendMessage(msg.getMessage(msg.getContextFactory().getContext(world)));
        SDCDoubleContextMessageFactory<MobType, String> item = messages.getWorldColorMapItemMessage();
        for (ColorMap<?> map : worldSettings.getEnabledColorMaps(true)) {
            MobType type = map.getApplicableEntityType();
            if (targetType != null && type != targetType) {
                continue;
            }
            sender.sendMessage(item.getMessage(item.getContextFactory().getContext(type, map.getName())));
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
