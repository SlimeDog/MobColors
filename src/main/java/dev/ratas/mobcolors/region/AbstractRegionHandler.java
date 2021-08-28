package dev.ratas.mobcolors.region;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import dev.ratas.mobcolors.config.mob.MobTypes;

public abstract class AbstractRegionHandler {

    protected boolean isApplicable(Entity entity, EntityType targetType, RegionInfo info) {
        if (targetType != null && !entity.getType().equals(targetType)) {
            return false;
        }
        Class<?> clazz = MobTypes.getInterestingClass(entity);
        if (clazz == null) {
            return false; // ignore - not of correct type
        }
        if (!(entity instanceof LivingEntity)) {
            return false; // ignore
        }
        if (!info.isInRange(entity)) {
            return false;
        }
        return true;
    }

}
