package dev.ratas.mobcolors.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldProvider {
    private final JavaPlugin plugin;

    public WorldProvider(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public World getWorld(String name) {
        return plugin.getServer().getWorld(name);
    }

    public Collection<String> getWorldNames() {
        List<String> names = new ArrayList<>();
        for (World world : plugin.getServer().getWorlds()) {
            names.add(world.getName());
        }
        return names;
    }

}
