package dev.ratas.mobcolors.commands.subcommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.util.StringUtil;

import dev.ratas.mobcolors.config.Messages;
import dev.ratas.mobcolors.config.Settings;
import dev.ratas.mobcolors.config.mob.MobType;
import dev.ratas.mobcolors.config.mob.MobTypes;
import dev.ratas.mobcolors.region.DistanceRegionInfo;
import dev.ratas.mobcolors.region.RegionInfo;
import dev.ratas.mobcolors.region.RegionMapper;
import dev.ratas.mobcolors.region.RegionOptions;
import dev.ratas.mobcolors.region.ScanReport;
import dev.ratas.mobcolors.utils.CommandUtils;
import dev.ratas.slimedogcore.api.commands.SDCCommandOptionSet;
import dev.ratas.slimedogcore.api.messaging.recipient.SDCRecipient;
import dev.ratas.slimedogcore.api.wrappers.SDCWorldProvider;

public class ColorSubCommand extends AbstractRegionSubCommand {
    private static final String NAME = "color";
    private static final String USAGE_REGION = "/mobcolors color region [ world-name x z ] [ --all | --leashed | --pets | --traders ] [ --mob <mob-type> ] [ --scan ]";
    private static final String USAGE_DISTANCE = "/mobcolors color distance <d> [ --all | --leashed | --pets | --traders ] [ --mob <mob-type> ] [ --scan ]";
    private static final String PERMS = "mobcolors.region";
    private static final List<String> FIRST_OPTIONS = Arrays.asList("region", "distance");
    private static final List<String> OPTIONS = Arrays.asList("--all", "--leashed", "--pets", "--scan", "--mob");
    private static final List<String> LLAMA_OPTIONS = Arrays.asList("--all", "--leashed", "--pets", "--traders",
            "--scan", "--mob");
    private final RegionMapper mapper;
    private final SDCWorldProvider worldProvider;
    private final Settings settings;
    private final Messages messages;

    public ColorSubCommand(RegionMapper mapper, SDCWorldProvider worldProvider, Settings settings, Messages messages) {
        super(settings, messages, NAME, USAGE_REGION + "\n" + USAGE_DISTANCE, PERMS, false, true);
        this.mapper = mapper;
        this.worldProvider = worldProvider;
        this.settings = settings;
        this.messages = messages;
    }

    @Override
    public String getUsage(SDCRecipient sender, String[] args) {
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
    public List<String> onTabComplete(SDCRecipient sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            if (sender.isPlayer()) {
                return StringUtil.copyPartialMatches(args[0], FIRST_OPTIONS, list);
            } else {
                List<String> options = new ArrayList<>(FIRST_OPTIONS);
                options.remove("distance");
                return StringUtil.copyPartialMatches(args[0], options, list);
            }
        }
        if (shouldShowOptions(sender, args)) {
            if (args[args.length - 2].equalsIgnoreCase("--mob")) { // after --mob, need entity type
                return StringUtil.copyPartialMatches(args[args.length - 1], MobTypes.ENTITY_TYPE_NAMES, list);
            }
            List<String> curOptions;
            MobType targetType = CommandUtils.getTargetType(args);
            if (targetType != null && targetType != MobType.llama) {
                curOptions = new ArrayList<>(OPTIONS);
            } else {
                curOptions = new ArrayList<>(LLAMA_OPTIONS);
            }
            for (String prevOption : CommandUtils.getOptions(args)) {
                curOptions.remove(prevOption);
                if (prevOption.equalsIgnoreCase("--all")) {
                    curOptions.remove("--all");
                    curOptions.remove("--pets");
                    curOptions.remove("--leashed");
                    curOptions.remove("--traders");
                }
            }
            return StringUtil.copyPartialMatches(args[args.length - 1], curOptions, list);
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("region")) {
                return StringUtil.copyPartialMatches(args[1], worldProvider.getAllWorldNames(), list);
            } else {
                return list; // no tab-completion for distance
            }
        }
        return list;
    }

    private boolean shouldShowOptions(SDCRecipient sender, String[] args) {
        if (sender.isPlayer()) {
            if (args.length > 1 && args[0].equalsIgnoreCase("region")) {
                if ((args.length <= 4 && args[1].startsWith("--")) || args.length > 4) {
                    return true;
                }
            } else if (args.length > 2 && args[0].equalsIgnoreCase("distance")) {
                return true;
            }
            return false;
        } else { // for console, only region
            return args.length > 4;
        }
    }

    private boolean hasValidArguments(SDCRecipient sender, String[] args) {
        if (args.length < 1) {
            return false;
        }
        if (args[0].equalsIgnoreCase("region")) {
            if (args.length < 4 && !sender.isPlayer()) {
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
    public boolean onOptionedCommand(SDCRecipient sender, String[] args, SDCCommandOptionSet options) {
        if (!hasValidArguments(sender, args)) {
            return false;
        }
        if (mapper.isBusy()) {
            sender.sendRawMessage(messages.getSchedulerBusyMessage());
            return true;
        }
        boolean isRegion = args[0].equalsIgnoreCase("region"); // otherwise distance
        boolean doLeashed = options.hasRawOption("--all") || options.hasRawOption("--leashed");
        boolean doPets = options.hasRawOption("--all") || options.hasRawOption("--pets");
        boolean ignoredUngenerated = !options.hasRawOption("--ungenerated");
        boolean showScan = options.hasRawOption("--scan");
        boolean specifyMob = options.hasRawOption("--mob");
        boolean doTraders = options.hasRawOption("--all") || options.hasRawOption("--traders");
        MobType targetType;
        if (specifyMob) {
            targetType = CommandUtils.getTargetType(args);
            if (targetType == null) {
                return false;
            }
        } else {
            targetType = null; // all
        }
        if (!showScan && specifyMob && settings.getSettings(targetType) == null) {
            sender.sendRawMessage(messages.getMobColorMapDisabledMessage(targetType));
            return true;
        }
        RegionInfo info;
        try {
            info = getRegionInfo(sender, args, isRegion, ignoredUngenerated, true);
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
                ? messages.getStartingToColorRegionMessage(info.getWorldDescriptor(), info.getStartChunkX() >> 5,
                        info.getStartChunkZ() >> 5, updateProgress, targetType)
                : messages.getStartingToColorRadiusMessage(info.getWorldDescriptor(),
                        ((DistanceRegionInfo) info).getMaxDistance(), updateProgress, targetType);
        sender.sendRawMessage(msg);
        mapper.dyeEntitiesInRegion(
                info, regionOptions, updateProgress, (done,
                        total) -> sender.sendRawMessage(isRegion ? messages.getUpdateOnColorRegionMessage(done, total)
                                : messages.getUpdateOnColorRadiusMessage(done, total)),
                showScan).whenComplete((result, e) -> {
                    ScanReport<?> colorReport = result.getColoringReport();
                    int mobsCounted = countAllMobs(colorReport);
                    sender.sendRawMessage(
                            messages.getDoneColoringRegionMessage(mobsCounted, colorReport.getChunksCounted()));
                    if (showScan) {
                        showReport(sender, result.getScanReport());
                    }
                });
        return true;
    }

}
