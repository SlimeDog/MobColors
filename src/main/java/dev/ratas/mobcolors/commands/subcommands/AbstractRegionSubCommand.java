package dev.ratas.mobcolors.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.ratas.mobcolors.commands.SimpleSubCommand;
import dev.ratas.mobcolors.config.Messages;

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

    protected RegionInfo getRegionInfo(CommandSender sender, String[] args) {
        if (args.length < 4 && sender instanceof Player) {
            if (args.length > 1) {
                throw new IllegalArgumentException();
            }
            Player player = (Player) sender;
            Location loc = player.getLocation();
            return new RegionInfo(player.getWorld(), loc.getBlockX() >> 9, loc.getBlockZ() >> 9);
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
        return new RegionInfo(world, regionX, regionZ);
    }

    protected class RegionInfo {
        protected final World world;
        protected final int x;
        protected final int z;

        private RegionInfo(World world, int x, int z) {
            this.world = world;
            this.x = x;
            this.z = z;
        }
    }

}
