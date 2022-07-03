package dev.ratas.mobcolors.commands.subcommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.util.StringUtil;

import dev.ratas.mobcolors.config.Messages;
import dev.ratas.mobcolors.config.Settings;
import dev.ratas.mobcolors.config.Messages.ProgressInfo;
import dev.ratas.mobcolors.config.mob.MobType;
import dev.ratas.mobcolors.config.mob.MobTypes;
import dev.ratas.mobcolors.region.DistanceRegionInfo;
import dev.ratas.mobcolors.region.RegionInfo;
import dev.ratas.mobcolors.region.RegionOptions;
import dev.ratas.mobcolors.region.RegionScanner;
import dev.ratas.mobcolors.utils.CommandUtils;
import dev.ratas.mobcolors.utils.WorldDescriptor;
import dev.ratas.slimedogcore.api.commands.SDCCommandOptionSet;
import dev.ratas.slimedogcore.api.messaging.factory.SDCQuadrupleContextMessageFactory;
import dev.ratas.slimedogcore.api.messaging.factory.SDCSingleContextMessageFactory;
import dev.ratas.slimedogcore.api.messaging.factory.SDCTripleContextMessageFactory;
import dev.ratas.slimedogcore.api.messaging.recipient.SDCRecipient;
import dev.ratas.slimedogcore.api.wrappers.SDCWorldProvider;

public class ScanSubCommand extends AbstractRegionSubCommand {
    private static final String NAME = "scan";
    private static final String USAGE_REGION = "/mobcolors scan region [ <world> <x> <z> ] [ --all | --leashed | --pets | --traders ] [ --mob <mob-type> ]";
    private static final String USAGE_DISTANCE = "/mobcolors scan distance <d> [ --all | --leashed | --pets | --traders ] [ --mob <mob-type> ]";
    private static final String PERMS = "mobcolors.scan";
    private static final List<String> FIRST_OPTIONS = Arrays.asList("region", "distance");
    private static final List<String> OPTIONS = Arrays.asList("--all", "--leashed", "--pets", "--mob");
    private static final List<String> LLAMA_OPTIONS = Arrays.asList("--all", "--leashed", "--pets", "--traders",
            "--scan", "--mob");
    private final RegionScanner scanner;
    private final SDCWorldProvider worldProvider;
    private final Messages messages;
    private final Settings settings;

    public ScanSubCommand(RegionScanner scanner, SDCWorldProvider worldProvider, Settings settings, Messages messages) {
        super(settings, messages, NAME, USAGE_REGION + "\n" + USAGE_DISTANCE, PERMS, false, true);
        this.scanner = scanner;
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
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("region")) {
                return StringUtil.copyPartialMatches(args[1], worldProvider.getAllWorldNames(), list);
            } else {
                return list; // no tab-completion for distance
            }
        }
        if (args.length > 4 || (args[0].equalsIgnoreCase("distance") && args.length > 2)) {
            if (args[args.length - 2].equalsIgnoreCase("--mob")) { // after --mob, need entity type
                return StringUtil.copyPartialMatches(args[args.length - 1], MobTypes.ENTITY_TYPE_NAMES, list);
            }
            List<String> curOptions;
            if (CommandUtils.getTargetType(args) != MobType.llama) {
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
        }
        return list;
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
        if (scanner.isBusy()) {
            sender.sendMessage(messages.getSchedulerBusyMessage().getMessage());
            return true;
        }
        boolean isRegion = args[0].equalsIgnoreCase("region");
        boolean doLeashed = options.hasRawOption("--all") || options.hasRawOption("--leashed");
        boolean doPets = options.hasRawOption("--all") || options.hasRawOption("--pets");
        boolean ignoredUngenerated = !options.hasRawOption("--ungenerated");
        boolean specifyMob = options.hasRawOption("--mob");
        boolean doTraders = options.hasRawOption("--all") || options.hasRawOption("--traders");
        MobType targetType;
        if (specifyMob) {
            targetType = options.getValue("mob", (name) -> CommandUtils.identifyType(name), null);
            if (targetType == null) {
                return false;
            }
        } else {
            targetType = null; // all
        }
        RegionInfo info;
        try {
            info = getRegionInfo(sender, args, isRegion, ignoredUngenerated, false);
        } catch (IllegalArgumentException e) {
            return false;
        }
        if (info == null) {
            return true;
        }
        RegionOptions regionOptions = new RegionOptions(targetType, !doPets, !doLeashed, !doTraders);
        double updateProgress = isRegion ? settings.scanRegionUpdateProgress() : settings.scanDistanceUpdateProgress();
        if (isRegion) {
            SDCQuadrupleContextMessageFactory<WorldDescriptor, Integer, Integer, MobType> msg = messages
                    .getStartingToScanRegionMessage();
            sender.sendMessage(msg.getMessage(
                    msg.getContextFactory().getContext(info.getWorldDescriptor(), info.getStartChunkX() >> 5,
                            info.getStartChunkZ() >> 5, targetType)));
        } else {
            SDCTripleContextMessageFactory<WorldDescriptor, Double, MobType> msg = messages
                    .getStartingToScanRadiusMessage();
            sender.sendMessage(msg.getMessage(msg.getContextFactory().getContext(info.getWorldDescriptor(),
                    ((DistanceRegionInfo) info).getMaxDistance(), targetType)));
        }
        SDCSingleContextMessageFactory<ProgressInfo> updater = isRegion ? messages.getUpdateOnScanRegionMessage()
                : messages.getUpdateOnScanRadiusMessage();
        scanner.scanRegion(info, regionOptions, updateProgress,
                (done, total) -> sender.sendMessage(
                        updater.getMessage(updater.getContextFactory().getContext(new ProgressInfo(done, total)))))
                .whenComplete((report, e) -> {
                    int mobsCounted = countAllMobs(report);
                    if (!specifyMob) { // if mob specified, the one in showReport will suffice
                        SDCTripleContextMessageFactory<Long, Long, MobType> msg = messages
                                .getDoneScanningHeaderMessage();
                        sender.sendMessage(msg.getMessage(msg.getContextFactory().getContext((long) mobsCounted,
                                report.getChunksCounted(), targetType)));
                    }
                    showReport(sender, report);
                });
        return true;
    }

}
