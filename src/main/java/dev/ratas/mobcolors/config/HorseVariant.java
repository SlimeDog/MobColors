package dev.ratas.mobcolors.config;

import org.bukkit.entity.Horse;

import dev.ratas.mobcolors.config.variants.TwoTypeComplexVariant;

public final class HorseVariant extends TwoTypeComplexVariant<Horse.Color, Horse.Style> {
    private static final InstanceTracker<Horse.Color, InstanceTracker<Horse.Style, HorseVariant>> INSTANCE_TRACKER = InstanceTracker
            .getBiTracker((color, style) -> new HorseVariant(color, style));
    public static final String DELIMITER = "/";

    private HorseVariant(Horse.Color color, Horse.Style style) {
        super(color, style);
    }

    @Override
    public String toString() {
        return getName();
    }

    public static HorseVariant valueOf(String name) {
        String[] keys = name.split(DELIMITER);
        int nr = keys.length;
        if (nr != 2) {
            throw new IllegalArgumentException(
                    "Wrong number of different types in key '" + name + "'. Expected " + 2 + " but got " + nr);
        }
        return getVariant(Horse.Color.valueOf(keys[0]), Horse.Style.valueOf(keys[1]));
    }

    public static HorseVariant getVariant(Horse horse) {
        return getVariant(horse.getColor(), horse.getStyle());
    }

    public static HorseVariant getVariant(Horse.Color color, Horse.Style style) {
        return INSTANCE_TRACKER.get(color).get(style);
    }

}
