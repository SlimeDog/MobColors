package dev.ratas.mobcolors;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public interface PluginProvider {

    boolean isPluginEnabled(String name);

    Plugin getPlugin(String name);

    public static class Implementation implements PluginProvider {
        private final JavaPlugin plugin;

        public Implementation(JavaPlugin plugin) {
            this.plugin = plugin;
        }

        @Override
        public boolean isPluginEnabled(String name) {
            return plugin.getServer().getPluginManager().isPluginEnabled(name);
        }

        @Override
        public Plugin getPlugin(String name) {
            return plugin.getServer().getPluginManager().getPlugin(name);
        }

    }

}
