package dev.ratas.mobcolors.region;

import org.bukkit.entity.EntityType;

public class RegionOptions {
    private final EntityType targetType;
    private final boolean ignorePets;
    private final boolean ignoreLeashed;
    private final boolean ignoreTraders;

    public RegionOptions(EntityType targetType, boolean ignorePets, boolean ignoreLeashed, boolean ignoreTraders) {
        this.targetType = targetType;
        this.ignorePets = ignorePets;
        this.ignoreLeashed = ignoreLeashed;
        this.ignoreTraders = ignoreTraders;
    }

    public boolean hasTargetType() {
        return targetType != null;
    }

    public EntityType getTargetType() {
        return targetType;
    }

    public boolean shouldIgnorePets() {
        return ignorePets;
    }

    public boolean shouldIgnoreLeashed() {
        return ignoreLeashed;
    }

    public boolean shouldIgnoreTraders() {
        return ignoreTraders;
    }

}
