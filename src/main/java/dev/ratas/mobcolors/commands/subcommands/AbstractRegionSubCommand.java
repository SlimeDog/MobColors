package dev.ratas.mobcolors.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.ratas.mobcolors.commands.SimpleSubCommand;
import dev.ratas.mobcolors.config.Messages;
import dev.ratas.mobcolors.region.DistanceRegionInfo;
import dev.ratas.mobcolors.region.MultiReport;
import dev.ratas.mobcolors.region.RectangularRegionInfo;
import dev.ratas.mobcolors.region.RegionInfo;
import dev.ratas.mobcolors.region.ScanReport;

public abstract class AbstractRegionSubCommand extends SimpleSubCommand {
    private final Messages messages;

    public AbstractRegionSubCommand(Messages messages, String name, String usage, String perms, boolean needsPlayer) {
        this(messages, name, usage, perms, needsPlayer, true);
    }

    public AbstractRegionSubCommand(Messages messages, String name, String usage, String perms, boolean needsPlayer,
            boolean showOnTabComplete) {
        super(name, usage, perms, needsPlayer, showOnTabComplete);
        this.messages = messages;
    }

    protected RegionInfo getRegionInfo(CommandSender sender, String[] args, boolean isRegion,
            boolean ignoredUngenerated) {
        if (!isRegion) { // distance
            if (!(sender instanceof Player)) {
                throw new IllegalArgumentException("Cannot use distance with console");
            }
            return getDistanceRegionInfo((Player) sender, args, ignoredUngenerated);
        }
        if (args.length < 4 && sender instanceof Player) {
            if (args.length > 1) {
                throw new IllegalArgumentException();
            }
            Player player = (Player) sender;
            Location loc = player.getLocation();
            return new RectangularRegionInfo(player.getWorld(), loc.getBlockX() >> 9, loc.getBlockZ() >> 9,
                    ignoredUngenerated);
        } else if (!(sender instanceof Player) && args.length < 4) {
            throw new IllegalArgumentException();
        }
        String worldName = args[1];
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            sender.sendMessage(messages.getWorldNotFoundMessage(worldName));
            return null;
        }
        int regionX, regionZ;
        try {
            regionX = Integer.parseInt(args[2]);
            regionZ = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            sender.sendMessage(messages.getNeedANumber(args[2], args[3]));
            return null;
        }
        return new RectangularRegionInfo(world, regionX, regionZ, ignoredUngenerated);
    }

    protected RegionInfo getDistanceRegionInfo(Player player, String[] args, boolean ignoreUngenerated) {
        if (args.length < 5) {
            throw new IllegalArgumentException("Too few argumens. Expected at least 5. Got " + args.length);
        }
        String arg4 = args[4];
        double dist;
        try {
            dist = Double.parseDouble(arg4);
        } catch (IllegalArgumentException e) {
            throw e;
        }
        return new DistanceRegionInfo(player.getLocation(), dist, ignoreUngenerated);
    }

    protected int countAllMobs(ScanReport<?> report) {
        if (report instanceof MultiReport) {
            int count = 0;
            for (ScanReport<?> part : ((MultiReport) report).getAllReports().values()) {
                count += countAllMobs(part);
            }
            return count;
        }
        return report.getColors().values().stream().mapToInt((i) -> i).sum();
    }

    protected void showReport(CommandSender sender, ScanReport<?> report) {
        if (report instanceof MultiReport) {
            for (ScanReport<?> part : ((MultiReport) report).getAllReports().values()) {
                showReport(sender, part);
            }
            return;
        }
        long mobsCounted = countAllMobs(report);
        long chunks = report.getChunksCounted();
        sender.sendMessage(messages.getDoneScanningHeaderMessage(mobsCounted, chunks, report.getType()));
        report.getColors().entrySet().forEach(
                (entry) -> sender.sendMessage(messages.getDoneScanningItemMessage(entry.getKey(), entry.getValue())));
    }

}
