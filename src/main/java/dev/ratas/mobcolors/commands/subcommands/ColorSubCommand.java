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

public class ColorSubCommand extends AbstractRegionSubCommand {
    private static final String NAME = "color";
    private static final String USAGE_REGION = "/mobcolors color region [ world-name x z ] [ --all ] [ --leashed ] [ --pets ]";
    private static final String USAGE_DISTANCE = "/mobcolors color distance <d> [ --all | --leashed | --pets ] [ --mob <mob-type> ]";
    private static final String PERMS = "mobcolors.region";
    private static final List<String> FIRST_OPTIONS = Arrays.asList("region", "distance");
    private static final List<String> OPTIONS = Arrays.asList("--all", "--leashed", "--pets", "--scan", "--mob");
    private static final List<String> ENTITY_TYPE_NAMES = MobTypes.ENTITY_COLOR_ENUMS.keySet().stream()
            .map(type -> type.name().toLowerCase()).collect(Collectors.toList());
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
            return StringUtil.copyPartialMatches(args[0], FIRST_OPTIONS, list);
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("region")) {
                return StringUtil.copyPartialMatches(args[1],
                        Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList()), list);
            } else if (args[0].equalsIgnoreCase("distance")) {
                return StringUtil.copyPartialMatches(args[1], OPTIONS, list);
            } else {
                return list; // no tab-completion for distance
            }
        }
        if (args.length > 4 || (args[0].equalsIgnoreCase("distance") && args.length > 2)) {
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
        boolean isRegion = args[0].equalsIgnoreCase("region"); // otherwise distance
        boolean doLeashed = options.contains("--all") || options.contains("--leashed");
        boolean doPets = options.contains("--all") || options.contains("--pets");
        boolean ignoredUngenerated = !options.contains("--ungenerated");
        boolean showScan = options.contains("--scan");
        boolean specifyMob = options.contains("--mob");
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
        long updateTicks = settings.ticksBetweenLongTaskUpdates();
        String msg = isRegion
                ? messages.getStartingToColorRegionMessage(info.getWorld(), info.getStartChunkX() >> 5,
                        info.getStartChunkZ() >> 5, updateTicks, targetType)
                : messages.getStartingToColorRadiusMessage(info.getWorld(),
                        ((DistanceRegionInfo) info).getMaxDistance(), updateTicks, targetType);
        sender.sendMessage(msg);
        mapper.dyeEntitiesInRegion(info, doLeashed, doPets, updateTicks,
                (done, total) -> sender.sendMessage(isRegion ? messages.getUpdateOnColorRegionMessage(done, total)
                        : messages.getUpdateOnColorRadiusMessage(done, total)),
                targetType).whenComplete((report, e) -> {
                    int mobsCounted = countAllMobs(report);
                    sender.sendMessage(messages.getDoneColoringRegionMessage(mobsCounted, report.getChunksCounted()));
                    if (showScan) {
                        showReport(sender, report);
                    }
                });
        return true;
    }

}
