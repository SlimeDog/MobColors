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
import dev.ratas.mobcolors.region.RegionScanner;

public class ScanSubCommand extends AbstractRegionSubCommand {
    private static final String NAME = "scan";
    private static final String USAGE = "/sheepspawncolors scan region [ world-name x z ] [ --all ] [ --leashed ] [ --pets ]";
    private static final String PERMS = "sheepspawncolors.scan";
    private static final List<String> FIRST_OPTIONS = Arrays.asList("region");
    private static final List<String> OPTIONS = Arrays.asList("--all", "--leashed", "--pets");
    private final RegionScanner scanner;
    private final Messages messages;
    private final Settings settings;

    public ScanSubCommand(RegionScanner scanner, Settings settings, Messages messages) {
        super(messages, NAME, USAGE, PERMS, false);
        this.scanner = scanner;
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
        boolean doLeashed = options.contains("--all") || options.contains("--leashed");
        boolean doPets = options.contains("--all") || options.contains("--pets");
        boolean ignoredUngenerated = !options.contains("--ungenerated");
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
                (done, total) -> sender.sendMessage(messages.getUpdateOnScanMessage(done, total)), ignoredUngenerated)
                .whenComplete((report, e) -> {
                    int sheepCounted = report.getColors().values().stream().mapToInt((i) -> i).sum();
                    sender.sendMessage(messages.getDoneScanningHeaderMessage(sheepCounted, report.getChunksCounted()));
                    report.getColors().entrySet().forEach((entry) -> sender
                            .sendMessage(messages.getDoneScanningItemMessage(entry.getKey(), entry.getValue())));
                });
        return true;
    }

}
