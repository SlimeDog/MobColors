package dev.ratas.mobcolors.region.version.mock;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.World;

import dev.ratas.mobcolors.utils.WorldProvider;

public class MockWorldProvider extends WorldProvider {

    public MockWorldProvider() {
        super(null);
    }

    public World getWorld(String name) {
        return null;
    }

    public Collection<String> getWorldNames() {
        return new ArrayList<>();
    }

}
