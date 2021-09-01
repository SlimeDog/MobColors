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
import dev.ratas.mobcolors.region.RegionMapper;
import dev.ratas.mobcolors.region.RegionOptions;
import dev.ratas.mobcolors.region.ScanReport;

public class ColorSubCommand extends AbstractRegionSubCommand {
    private static final String NAME = "color";
    private static final String USAGE_REGION = "/mobcolors color region [ world-name x z ] [ --all ] [ --leashed ] [ --pets ]";
    private static final String USAGE_DISTANCE = "/mobcolors color distance <d> [ --all | --leashed | --pets ] [ --mob <mob-type> ]";
    private static final String PERMS = "mobcolors.region";
    private static final List<String> FIRST_OPTIONS = Arrays.asList("region", "distance");
    private static final List<String> OPTIONS = Arrays.asList("--all", "--leashed", "--pets", "--scan", "--mob");
    private static final List<String> LLAMA_OPTIONS = Arrays.asList("--all", "--leashed", "--pets", "--traders",
            "--scan", "--mob");
    private final RegionMapper mapper;
    private final Settings settings;
    private final Messages messages;

    public ColorSubCommand(RegionMapper mapper, Settings settings, Messages messages) {
        super(messages, NAME, USAGE_REGION + "\n" + USAGE_DISTANCE, PERMS, false);
        this.mapper = mapper;
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
        Set<String> options = getOptions(args);
        boolean isRegion = args[0].equalsIgnoreCase("region"); // otherwise distance
        boolean doLeashed = options.contains("--all") || options.contains("--leashed");
        boolean doPets = options.contains("--all") || options.contains("--pets");
        boolean ignoredUngenerated = !options.contains("--ungenerated");
        boolean showScan = options.contains("--scan");
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
        if (!showScan && specifyMob && settings.getSettings(targetType) == null) {
            sender.sendMessage(messages.getMobColorMapDisabledMessage(targetType));
            return true;
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
        double updateProgress = isRegion ? settings.colorRegionUpdateProgress()
                : settings.colorDistanceUpdateProgress();
        String msg = isRegion
                ? messages.getStartingToColorRegionMessage(info.getWorld(), info.getStartChunkX() >> 5,
                        info.getStartChunkZ() >> 5, updateProgress, targetType)
                : messages.getStartingToColorRadiusMessage(info.getWorld(),
                        ((DistanceRegionInfo) info).getMaxDistance(), updateProgress, targetType);
        sender.sendMessage(msg);
        mapper.dyeEntitiesInRegion(
                info, regionOptions, updateProgress, (done,
                        total) -> sender.sendMessage(isRegion ? messages.getUpdateOnColorRegionMessage(done, total)
                                : messages.getUpdateOnColorRadiusMessage(done, total)),
                showScan).whenComplete((result, e) -> {
                    ScanReport<?> colorReport = result.getColoringReport();
                    int mobsCounted = countAllMobs(colorReport);
                    sender.sendMessage(
                            messages.getDoneColoringRegionMessage(mobsCounted, colorReport.getChunksCounted()));
                    if (showScan) {
                        showReport(sender, result.getScanReport());
                    }
                });
        return true;
    }

}
