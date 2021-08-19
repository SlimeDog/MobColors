package dev.ratas.mobcolors.events;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class ListenerRegistrator {
    private final JavaPlugin plugin;

    public ListenerRegistrator(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Listener listeer) {
        plugin.getServer().getPluginManager().registerEvents(listeer, plugin);
    }
    
}
