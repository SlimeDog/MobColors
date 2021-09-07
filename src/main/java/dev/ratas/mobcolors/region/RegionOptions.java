package dev.ratas.mobcolors.region;

import dev.ratas.mobcolors.config.mob.MobType;

public class RegionOptions {
    private final MobType targetType;
    private final boolean ignorePets;
    private final boolean ignoreLeashed;
    private final boolean ignoreTraders;

    public RegionOptions(MobType targetType, boolean ignorePets, boolean ignoreLeashed, boolean ignoreTraders) {
        this.targetType = targetType;
        this.ignorePets = ignorePets;
        this.ignoreLeashed = ignoreLeashed;
        this.ignoreTraders = ignoreTraders;
    }

    public boolean hasTargetType() {
        return targetType != null;
    }

    public MobType getTargetType() {
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
