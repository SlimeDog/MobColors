package dev.ratas.mobcolors.region.version.mock;

import java.util.Arrays;
import java.util.Collection;

import org.bukkit.World;

import dev.ratas.mobcolors.utils.WorldProvider;

public class MockWorldProvider extends WorldProvider {
    private final MockWorld world;

    public MockWorldProvider(MockWorld world) {
        super(null);
        this.world = world;
    }

    public World getWorld(String name) {
        return world;
    }

    public Collection<String> getWorldNames() {
        return Arrays.asList(world.getName());
    }

}
