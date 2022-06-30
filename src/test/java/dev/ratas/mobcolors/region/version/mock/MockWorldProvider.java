package dev.ratas.mobcolors.region.version.mock;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.World;

import dev.ratas.slimedogcore.api.wrappers.SDCWorldProvider;

public class MockWorldProvider implements SDCWorldProvider {
    private final MockWorld world;

    public MockWorldProvider(MockWorld world) {
        this.world = world;
    }

    public Collection<String> getWorldNames() {
        return Arrays.asList(world.getName());
    }

    @Override
    public World getWorldByName(String name) {
        return world;
    }

    @Override
    public World getWorldById(UUID id) {
        return world;
    }

    @Override
    public List<World> getAllWorlds() {
        return Arrays.asList(world);
    }

}
