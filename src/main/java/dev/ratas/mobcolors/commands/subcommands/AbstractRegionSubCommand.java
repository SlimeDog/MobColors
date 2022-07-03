package dev.ratas.mobcolors.commands.subcommands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import dev.ratas.mobcolors.config.Messages;
import dev.ratas.mobcolors.config.Settings;
import dev.ratas.mobcolors.config.mob.MobType;
import dev.ratas.mobcolors.region.DistanceRegionInfo;
import dev.ratas.mobcolors.region.MultiReport;
import dev.ratas.mobcolors.region.RectangularRegionInfo;
import dev.ratas.mobcolors.region.RegionInfo;
import dev.ratas.mobcolors.region.ScanReport;
import dev.ratas.slimedogcore.api.messaging.factory.SDCDoubleContextMessageFactory;
import dev.ratas.slimedogcore.api.messaging.factory.SDCSingleContextMessageFactory;
import dev.ratas.slimedogcore.api.messaging.factory.SDCTripleContextMessageFactory;
import dev.ratas.slimedogcore.api.messaging.recipient.SDCPlayerRecipient;
import dev.ratas.slimedogcore.api.messaging.recipient.SDCRecipient;
import dev.ratas.slimedogcore.impl.commands.AbstractSubCommand;

public abstract class AbstractRegionSubCommand extends AbstractSubCommand {
    private final Settings settings;
    private final Messages messages;

    public AbstractRegionSubCommand(Settings settings, Messages messages, String name, String usage, String perms,
            boolean needsPlayer) {
        this(settings, messages, name, usage, perms, needsPlayer, true);
    }

    public AbstractRegionSubCommand(Settings settings, Messages messages, String name, String usage, String perms,
            boolean needsPlayer, boolean showOnTabComplete) {
        super(name, perms, usage, showOnTabComplete, needsPlayer);
        this.settings = settings;
        this.messages = messages;
    }

    protected RegionInfo getRegionInfo(SDCRecipient sender, String[] args, boolean isRegion,
            boolean ignoredUngenerated, boolean isColor) {
        if (!isRegion) { // distance
            if (!sender.isPlayer()) {
                throw new IllegalArgumentException("Cannot use distance with console");
            }
            return getDistanceRegionInfo((SDCPlayerRecipient) sender, args, ignoredUngenerated, isColor);
        }
        if (args.length < 4 && sender.isPlayer()) {
            if (args.length > 1 // if option (i.e --scan) specified then it's fine
                    && Arrays.stream(Arrays.copyOfRange(args, 1, args.length)).anyMatch(arg -> !arg.startsWith("--"))) {
                throw new IllegalArgumentException();
            }
            SDCPlayerRecipient player = (SDCPlayerRecipient) sender;
            Location loc = player.getLocation();
            return new RectangularRegionInfo(loc.getWorld(), loc.getBlockX() >> 9, loc.getBlockZ() >> 9,
                    ignoredUngenerated);
        } else if (!sender.isPlayer() && args.length < 4) {
            throw new IllegalArgumentException();
        }
        String worldName = args[1];
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            SDCSingleContextMessageFactory<String> msg = messages.getWorldNotFoundMessage();
            sender.sendMessage(msg.getMessage(msg.getContextFactory().getContext(worldName)));
            return null;
        }
        int regionX, regionZ;
        try {
            regionX = Integer.parseInt(args[2]);
            regionZ = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            SDCSingleContextMessageFactory<String[]> msg = messages.getNeedANumber();
            sender.sendMessage(msg.getMessage(msg.getContextFactory().getContext(new String[] { args[2], args[3] })));
            return null;
        }
        return new RectangularRegionInfo(world, regionX, regionZ, ignoredUngenerated);
    }

    protected RegionInfo getDistanceRegionInfo(SDCPlayerRecipient player, String[] args, boolean ignoreUngenerated,
            boolean isColor) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Too few argumens. Expected at least 2. Got " + args.length);
        }
        String arg = args[1];
        double dist;
        try {
            dist = Double.parseDouble(arg);
        } catch (IllegalArgumentException e) {
            throw e;
        }
        double maxDist = settings.maxDistanceForCommands();
        if (dist > maxDist) {
            SDCSingleContextMessageFactory<Double> msg = isColor ? messages.getColorDistanceTooBig()
                    : messages.getScanDistanceTooBig();
            player.sendMessage(msg.getMessage(msg.getContextFactory().getContext(maxDist)));
            return null;
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

    protected void showReport(SDCRecipient sender, ScanReport<?> report) {
        if (report instanceof MultiReport) {
            for (ScanReport<?> part : ((MultiReport) report).getAllReports().values()) {
                showReport(sender, part);
            }
            return;
        }
        long mobsCounted = countAllMobs(report);
        long chunks = report.getChunksCounted();
        SDCTripleContextMessageFactory<Long, Long, MobType> msg = messages.getDoneScanningHeaderMessage();
        sender.sendMessage(msg.getMessage(msg.getContextFactory().getContext(mobsCounted, chunks, report.getType())));
        report.getSortedColors().forEach(
                (entry) -> {

                    SDCDoubleContextMessageFactory<Integer, Object> m = messages.getDoneScanningItemMessage();
                    sender.sendMessage(
                            m.getMessage(m.getContextFactory().getContext(entry.getValue(), entry.getKey())));
                });
    }

}
