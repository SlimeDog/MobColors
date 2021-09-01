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
import dev.ratas.mobcolors.config.mob.MobTypes;
import dev.ratas.mobcolors.region.DistanceRegionInfo;
import dev.ratas.mobcolors.region.RegionInfo;
import dev.ratas.mobcolors.region.RegionOptions;
import dev.ratas.mobcolors.region.RegionScanner;

public class ScanSubCommand extends AbstractRegionSubCommand {
    private static final String NAME = "scan";
    private static final String USAGE_REGION = "/mobcolors scan region [ <world> <x> <z> ] [ --all | --leashed | --pets ] [ --mob <mob-type> ]";
    private static final String USAGE_DISTANCE = "/mobcolors scan distance <d> [ --all | --leashed | --pets ] [ --mob <mob-type> ]";
    private static final String PERMS = "mobcolors.scan";
    private static final List<String> FIRST_OPTIONS = Arrays.asList("region", "distance");
    private static final List<String> OPTIONS = Arrays.asList("--all", "--leashed", "--pets", "--mob");
    private static final List<String> LLAMA_OPTIONS = Arrays.asList("--all", "--leashed", "--pets", "--traders",
            "--scan", "--mob");
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
            if (sender instanceof Player) {
                return StringUtil.copyPartialMatches(args[0], FIRST_OPTIONS, list);
            } else {
                List<String> options = new ArrayList<>(FIRST_OPTIONS);
                options.remove("distance");
                return StringUtil.copyPartialMatches(args[0], options, list);
            }
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("region")) {
                return StringUtil.copyPartialMatches(args[1],
                        Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList()), list);
            } else {
                return list; // no tab-completion for distance
            }
        }
        if (args.length > 4 || (args[0].equalsIgnoreCase("distance") && args.length > 2)) {
            if (args[args.length - 2].equalsIgnoreCase("--mob")) { // after --mob, need entity type
                return StringUtil.copyPartialMatches(args[args.length - 1], MobTypes.ENTITY_TYPE_NAMES, list);
            }
            List<String> curOptions;
            if (getTargetType(args) != EntityType.LLAMA) {
                curOptions = new ArrayList<>(OPTIONS);
            } else {
                curOptions = new ArrayList<>(LLAMA_OPTIONS);
            }
            for (String prevOption : getOptions(args)) {
                curOptions.remove(prevOption);
                if (prevOption.equalsIgnoreCase("--all")) {
                    curOptions.remove("--all");
                    curOptions.remove("--pets");
                    curOptions.remove("--leashed");
                    curOptions.remove("--traders");
                }
            }
            return StringUtil.copyPartialMatches(args[args.length - 1], curOptions, list);
        }
        return list;
    }

    private boolean hasValidArguments(CommandSender sender, String[] args) {
        if (args.length < 1) {
            return false;
        }
        if (args[0].equalsIgnoreCase("region")) {
            if (args.length < 4 && !(sender instanceof Player)) {
                return false;
            }
            return true;
        } else if (args[0].equalsIgnoreCase("distance")) {
            if (args.length < 2) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean executeCommand(CommandSender sender, String[] args) {
        if (!hasValidArguments(sender, args)) {
            return false;
        }
        boolean isRegion = args[0].equalsIgnoreCase("region");
        Set<String> options = getOptions(args);
        boolean doLeashed = options.contains("--all") || options.contains("--leashed");
        boolean doPets = options.contains("--all") || options.contains("--pets");
        boolean ignoredUngenerated = !options.contains("--ungenerated");
        boolean specifyMob = options.contains("--mob");
        boolean doTraders = options.contains("--all") || options.contains("--traders");
        EntityType targetType;
        if (specifyMob) {
            targetType = getTargetType(args);
            if (targetType == null) {
                return false;
            }
        } else {
            targetType = null; // all
        }
        RegionInfo info;
        try {
            info = getRegionInfo(sender, args, isRegion, ignoredUngenerated);
        } catch (IllegalArgumentException e) {
            return false;
        }
        if (info == null) {
            return true;
        }
        RegionOptions regionOptions = new RegionOptions(targetType, !doPets, !doLeashed, !doTraders);
        double updateProgress = isRegion ? settings.scanRegionUpdateProgress() : settings.scanDistanceUpdateProgress();
        String msg = isRegion
                ? messages.getStartingToScanRegionMessage(info.getWorld(), info.getStartChunkX() >> 5,
                        info.getStartChunkZ() >> 5, updateProgress)
                : messages.getStartingToScanRadiusMessage(info.getWorld(), ((DistanceRegionInfo) info).getMaxDistance(),
                        updateProgress, targetType);
        sender.sendMessage(msg);
        scanner.scanRegion(info, regionOptions, updateProgress,
                (done, total) -> sender.sendMessage(isRegion ? messages.getUpdateOnScanRegionMessage(done, total)
                        : messages.getUpdateOnScanRadiusMessage(done, total)))
                .whenComplete((report, e) -> {
                    int mobsCounted = countAllMobs(report);
                    if (!specifyMob) { // if mob specified, the one in showReport will suffice
                        sender.sendMessage(messages.getDoneScanningHeaderMessage(mobsCounted, report.getChunksCounted(),
                                targetType));
                    }
                    showReport(sender, report);
                });
        return true;
    }

}
