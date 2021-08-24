package dev.ratas.mobcolors.commands.subcommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import dev.ratas.mobcolors.config.Messages;
import dev.ratas.mobcolors.config.Settings;
import dev.ratas.mobcolors.region.RegionScanner;

public class ScanSubCommand extends AbstractRegionSubCommand {
    private static final String NAME = "scan";
    private static final String USAGE_REGION = "/mobcolors scan region [ <world> <x> <z> ] [ --all | --leashed | --pets ] [ --mob <mob-type> ]";
    private static final String USAGE_DISTANCE = "/mobcolors scan distance <d> [ --all | --leashed | --pets ] [ --mob <mob-type> ]";
    private static final String PERMS = "mobcolors.scan";
    private static final List<String> FIRST_OPTIONS = Arrays.asList("region", "distance");
    private static final List<String> OPTIONS = Arrays.asList("--all", "--leashed", "--pets", "--mob");
    private static final List<String> ENTITY_TYPE_NAMES = Arrays.stream(EntityType.values())
            .map((et) -> et.name().toLowerCase()).collect(Collectors.toList());
    private final RegionScanner scanner;
    private final Messages messages;
    private final Settings settings;

    public ScanSubCommand(RegionScanner scanner, Settings settings, Messages messages) {
        super(messages, NAME, USAGE_REGION + "\n" + USAGE_DISTANCE, PERMS, false);
        this.scanner = scanner;
        this.settings = settings;
        this.messages = messages;
    }

    @Override
    public String getUsage(CommandSender sender, String[] args) {
        if (args.length == 0) {
            return super.getUsage(sender, args);
        } else if (args[0].equalsIgnoreCase("region")) {
            return USAGE_REGION;
        } else if (args[0].equalsIgnoreCase("distance")) {
            return USAGE_DISTANCE;
        }
        return super.getUsage(sender, args);
    }

    @Override
    public List<String> getTabComletions(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], FIRST_OPTIONS, list);
        }
        if (args.length == 2) {
            return StringUtil.copyPartialMatches(args[1],
                    Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList()), list);
        }
        if (args.length > 4) {
            if (args[args.length - 2].equalsIgnoreCase("--mob")) { // after --mob, need entity type
                return StringUtil.copyPartialMatches(args[args.length - 1], ENTITY_TYPE_NAMES, list);
            }
            List<String> curOptions = new ArrayList<>(OPTIONS);
            for (String prevOption : getOptions(args)) {
                curOptions.remove(prevOption);
                if (prevOption.equalsIgnoreCase("--all")) {
                    curOptions.remove("--all");
                    curOptions.remove("--pets");
                    curOptions.remove("--leashed");
                }
            }
            return StringUtil.copyPartialMatches(args[args.length - 1], curOptions, list);
        }
        return list;
    }

    @Override
    public boolean executeCommand(CommandSender sender, String[] args) {
        if ((args.length < 4 && !(sender instanceof Player)) || args.length < 1
                || !args[0].equalsIgnoreCase("region")) {
            return false;
        }
        Set<String> options = getOptions(args);
        boolean doLeashed = options.contains("--all") || options.contains("--leashed");
        boolean doPets = options.contains("--all") || options.contains("--pets");
        boolean ignoredUngenerated = !options.contains("--ungenerated");
        boolean specifyMob = options.contains("--mob");
        EntityType targetType;
        if (specifyMob) {
            targetType = getTargetType(args);
            if (targetType == null) {
                sender.sendMessage(getUsage(sender, args));
                return true;
            }
        } else {
            targetType = null; // all
        }
        RegionInfo info;
        try {
            info = getRegionInfo(sender, args);
        } catch (IllegalArgumentException e) {
            return false;
        }
        if (info == null) {
            return true;
        }
        long updateTicks = settings.ticksBetweenLongTaskUpdates();
        sender.sendMessage(messages.getStartingToScanMessage(info.world, info.x, info.z, updateTicks));
        scanner.scanRegion(info.world, info.x, info.z, doLeashed, doPets, updateTicks,
                (done, total) -> sender.sendMessage(messages.getUpdateOnScanMessage(done, total)), ignoredUngenerated,
                targetType).whenComplete((report, e) -> {
                    int sheepCounted = report.getColors().values().stream().mapToInt((i) -> i).sum();
                    sender.sendMessage(messages.getDoneScanningHeaderMessage(sheepCounted, report.getChunksCounted()));
                    report.getColors().entrySet().forEach((entry) -> sender
                            .sendMessage(messages.getDoneScanningItemMessage(entry.getKey(), entry.getValue())));
                });
        return true;
    }

    private EntityType getTargetType(String[] args) {
        boolean optionFound = false;
        for (String arg : args) {
            if (optionFound) {
                try {
                    return EntityType.valueOf(arg.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return null;
                }
            }
            if (arg.equalsIgnoreCase("--mob")) {
                optionFound = true;
            }
        }
        return null; // not enough arguments
    }

}
