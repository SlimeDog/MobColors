package dev.ratas.mobcolors.utils;

import java.util.Objects;
import java.util.UUID;

import org.bukkit.World;

public class WorldDescriptor {
    private final UUID id;
    private final String name;

    public WorldDescriptor(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean representsWorld(World world) {
        return world.getUID().equals(id) && world.getName().equals(name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof WorldDescriptor)) {
            return false;
        }
        WorldDescriptor o = (WorldDescriptor) other;
        return o.id.equals(id) && o.name.equals(name);
    }

    public static WorldDescriptor wrap(World world) {
        return new WorldDescriptor(world.getUID(), world.getName());
    }

}
