package dev.ratas.mobcolors.config;

import org.bukkit.entity.Horse;

import dev.ratas.mobcolors.config.variants.HorseColor;
import dev.ratas.mobcolors.config.variants.HorseStyle;
import dev.ratas.mobcolors.config.variants.TwoTypeComplexVariant;

public final class HorseVariant extends TwoTypeComplexVariant<HorseColor, HorseStyle> {
    private static final InstanceTracker<HorseColor, InstanceTracker<HorseStyle, HorseVariant>> INSTANCE_TRACKER = InstanceTracker
            .getBiTracker((color, style) -> new HorseVariant(color, style));
    public static final String DELIMITER = "/";

    private HorseVariant(HorseColor color, HorseStyle style) {
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
        return getVariant(HorseColor.valueOf(keys[0]), HorseStyle.valueOf(keys[1]));
    }

    public static HorseVariant getVariant(Horse horse) {
        return getVariant(HorseColor.getType(horse.getColor()), HorseStyle.getType(horse.getStyle()));
    }

    public static HorseVariant getVariant(HorseColor color, HorseStyle style) {
        return INSTANCE_TRACKER.get(color).get(style);
    }

}
