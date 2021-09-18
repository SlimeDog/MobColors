package dev.ratas.mobcolors.region;

import java.util.EnumMap;
import java.util.Map;

import org.bukkit.entity.Entity;

import dev.ratas.mobcolors.config.mob.MobType;
import dev.ratas.mobcolors.config.mob.MobTypes;

public class MultiReport extends ScanReport<Object> {
    private final Map<MobType, ScanReport<?>> typedReports = new EnumMap<>(MobType.class);

    public MultiReport() {
        super(null, null); // no function needed since the count method is overwritten
    }

    @Override
    public void count(Entity entity) {
        MobType type = MobType.getType(entity.getType());
        if (type == null) {
            return; // TODO - show warning
        }
        ScanReport<?> report = typedReports.computeIfAbsent(type, t -> {
            Class<?> clazz = MobTypes.getInterestingClass(entity);
            if (clazz == null) {
                throw new IllegalArgumentException(
                        "Entiy of type " + t + " is not colorable (yet is attempted to be counted)");
            }
            ScanReport<?> sr = new ScanReport<>(t, MobTypes.getFunctionFor(entity));
            if (!typedReports.isEmpty()) {
                for (int i = 0; i < getChunksCounted(); i++) {
                    sr.countAChunk();
                }
            }
            return sr;
        });
        report.count(entity);
    }

    @Override
    public void countAChunk() {
        super.countAChunk();
        for (ScanReport<?> report : typedReports.values()) {
            report.countAChunk(); // propagate down
        }
    }

    public Map<MobType, ScanReport<?>> getAllReports() {
        return typedReports;
    }

    @Override
    public MobType getType() {
        throw new IllegalStateException("Cannot directly get type from multi report. "
                + "Use MultiReport#getAllReports and get the type from each one separately.");
    }

    @Override
    public Map<Object, Integer> getColors() {
        throw new IllegalStateException("Cannot directly get colors from multi report. "
                + "Use MultiReport#getAllReports and get the colors from each one separately.");
    }

}
