package dev.ratas.mobcolors.config;

import org.bukkit.entity.Horse;

import dev.ratas.mobcolors.utils.Pair;

public final class HorseVariant extends Pair<Horse.Color, Horse.Style> {
    private static final String DELIMITER = "/";

    private HorseVariant(Horse.Color color, Horse.Style style) {
        super(color, style);
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

}
