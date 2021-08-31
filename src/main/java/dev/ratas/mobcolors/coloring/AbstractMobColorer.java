package dev.ratas.mobcolors.coloring;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import dev.ratas.mobcolors.coloring.settings.ColorChoice;
import dev.ratas.mobcolors.coloring.settings.ColorMap;
import dev.ratas.mobcolors.coloring.settings.ColorDecision;
import dev.ratas.mobcolors.coloring.settings.ColorReason;
import dev.ratas.mobcolors.config.mob.MobSettings;
import dev.ratas.mobcolors.region.RegionOptions;
import dev.ratas.mobcolors.utils.PetUtils;

public abstract class AbstractMobColorer<E, T> implements MobColorer<E, T> {
    private final MobSettings settings;
    private final ColorMap<T> colorMap;

    public AbstractMobColorer(MobSettings settings, ColorMap<T> colorMap) {
        this.colorMap = colorMap;
        this.settings = settings;
    }

    @Override
    public ColorMap<T> getColorMap() {
        return colorMap;
    }

    @Override
    public ColorDecision shouldColor(E entity, RegionOptions options, ColorReason reason) {
        // TODO - fix casting below - it's not guaranteed to be safe
        Entity ent = (Entity) entity;
        MobSettings settings = options != null ? this.settings.wrapWith(options) : this.settings;
        if (!settings.shouldIncludePets() && PetUtils.isPet(ent)) {
            return ColorDecision.IGNORE;
        }
        if (!settings.shouldIncludeLeashed() && ent instanceof LivingEntity && ((LivingEntity) ent).isLeashed()) {
            return ColorDecision.IGNORE;
        }
        if (ent.getType() == EntityType.TRADER_LLAMA && !settings.shouldIncludeTraders()) {
            return ColorDecision.IGNORE;
        }
        return reason == ColorReason.SPAWN ? ColorDecision.RESCHEDULE : ColorDecision.COLOR;
    }

    protected ColorChoice<T> rollColor() {
        return colorMap.rollColor();
    }

}
