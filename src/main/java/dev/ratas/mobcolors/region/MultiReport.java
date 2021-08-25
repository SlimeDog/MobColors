package dev.ratas.mobcolors.region;

import java.util.EnumMap;
import java.util.Map;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import dev.ratas.mobcolors.config.mob.MobTypes;

public class MultiReport extends ScanReport<Object> {
    private final Map<EntityType, ScanReport<?>> typedReports = new EnumMap<>(EntityType.class);

    public MultiReport() {
        super(null, null); // no function needed since the count method is overwritten
    }

    @Override
    public void count(Entity entity) {
        EntityType type = entity.getType();
        ScanReport<?> report = typedReports.computeIfAbsent(type, t -> {
            Class<?> clazz = MobTypes.getInterestingClass(entity);
            if (clazz == null) {
                throw new IllegalArgumentException(
                        "Entiy of type " + t + " is not colorable (yet is attempted to be counted)");
            }
            return new ScanReport<>(t, MobTypes.getFunctionFor(entity));
        });
        report.count(entity);
    }

    public void countAChunk() {
        super.countAChunk();
        for (ScanReport<?> report : typedReports.values()) {
            report.countAChunk(); // propagate down
        }
    }

    public Map<EntityType, ScanReport<?>> getAllReports() {
        return typedReports;
    }

    public EntityType getType() {
        throw new IllegalStateException("Cannot direcly get type from multi report. "
                + "Use MultiReport#getAllReports and get the type from each one separately.");
    }

    public Map<Object, Integer> getColors() {
        throw new IllegalStateException("Cannot direcly get colors from multi report. "
                + "Use MultiReport#getAllReports and get the colors from each one separately.");
    }

}
