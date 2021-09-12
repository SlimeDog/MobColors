package dev.ratas.mobcolors.config.mock;

import org.bukkit.plugin.Plugin;

import dev.ratas.mobcolors.PluginProvider;

public class MockPluginProvider implements PluginProvider {

    @Override
    public boolean isPluginEnabled(String name) {
        return false;
    }

    @Override
    public Plugin getPlugin(String name) {
        return null;
    }

}
