package dev.ratas.mobcolors.config;

import java.util.EnumMap;
import java.util.Map;

import org.bukkit.entity.Horse;

import dev.ratas.mobcolors.utils.Pair;

public final class HorseVariant extends Pair<Horse.Color, Horse.Style> {
    private static final InstanceTracker INSTANCE_TRACKER = new InstanceTracker();
    public static final String DELIMITER = "/";

    private HorseVariant(Horse.Color color, Horse.Style style) {
        super(color, style);
    }

    public String getName() {
        return getOne().name() + "/" + getTwo().name();
    }

    public static HorseVariant valueOf(String name) {
        String[] keys = name.split(DELIMITER);
        int nr = keys.length;
        if (nr != 2) {
            throw new IllegalArgumentException(
                    "Wrong number of different types in key '" + name + "'. Expected " + 2 + " but got " + nr);
        }
        return new HorseVariant(Horse.Color.valueOf(keys[0]), Horse.Style.valueOf(keys[1]));
    }

    public static HorseVariant getVariant(Horse horse) {
        return INSTANCE_TRACKER.get(horse.getColor(), horse.getStyle());
    }

    private static class InstanceTracker {
        private final Map<Horse.Color, Map<Horse.Style, HorseVariant>> variantsMap = new EnumMap<>(Horse.Color.class);

        private HorseVariant get(Horse.Color color, Horse.Style style) {
            Map<Horse.Style, HorseVariant> map = variantsMap.computeIfAbsent(color,
                    c -> new EnumMap<>(Horse.Style.class));
            return map.computeIfAbsent(style, s -> new HorseVariant(color, style));
        }
    }

}
