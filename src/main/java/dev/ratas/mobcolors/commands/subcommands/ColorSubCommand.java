package dev.ratas.mobcolors.commands.subcommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import dev.ratas.mobcolors.config.Messages;
import dev.ratas.mobcolors.config.Settings;
import dev.ratas.mobcolors.region.RegionInfo;
import dev.ratas.mobcolors.region.RegionMapper;

public class ColorSubCommand extends AbstractRegionSubCommand {
    private static final String NAME = "color";
    private static final String USAGE = "/mobcolors color region [ world-name x z ] [ --all ] [ --leashed ] [ --pets ]";
    private static final String PERMS = "mobcolors.region";
    private static final List<String> FIRST_OPTIONS = Arrays.asList("region");
    private static final List<String> OPTIONS = Arrays.asList("--all", "--leashed", "--pets", "--scan");
    private final RegionMapper mapper;
    private final Settings settings;
    private final Messages messages;

    public ColorSubCommand(RegionMapper mapper, Settings settings, Messages messages) {
        super(messages, NAME, USAGE, PERMS, false);
        this.mapper = mapper;
        this.settings = settings;
        this.messages = messages;
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
        sender.sendMessage(messages.getStartingToColorMessage(info.getWorld(), info.getStartChunkX() >> 5,
                info.getStartChunkZ() >> 5, updateTicks));
        mapper.dyeEntitiesInRegion(info, doLeashed, doPets, updateTicks,
                (done, total) -> sender.sendMessage(messages.getUpdateOnColorMessage(done, total)))
                .whenComplete((report, e) -> {
                    int sheepCounted = report.getColors().values().stream().mapToInt((i) -> i).sum();
                    sender.sendMessage(messages.getDoneColoringMessage(sheepCounted, report.getChunksCounted()));
                    if (showScan) {
                        report.getColors().entrySet().forEach((entry) -> sender
                                .sendMessage(messages.getDoneScanningItemMessage(entry.getKey(), entry.getValue())));
                    }
                });
        return true;
    }

}
