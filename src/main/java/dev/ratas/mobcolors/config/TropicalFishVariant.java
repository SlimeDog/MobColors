package dev.ratas.mobcolors.config;

import org.bukkit.DyeColor;
import org.bukkit.entity.TropicalFish;

import dev.ratas.mobcolors.utils.Triple;

public class TropicalFishVariant extends Triple<TropicalFish.Pattern, DyeColor, DyeColor> {
    private static final String DELIMITER = "/";

    private TropicalFishVariant(TropicalFish.Pattern pattern, DyeColor color1, DyeColor color2) {
        super(pattern, color1, color2);
    }

    public String getName() {
        return getOne().name() + "/" + getTwo().name() + "/" + getThree().name();
    }

    public static TropicalFishVariant valueOf(String name) {
        String[] keys = name.split(DELIMITER);
        int nr = keys.length;
        if (nr != 3) {
            throw new IllegalArgumentException(
                    "Wrong number of different types in key '" + name + "'. Expected " + 3 + " but got " + nr);
        }
        return new TropicalFishVariant(TropicalFish.Pattern.valueOf(keys[0]), DyeColor.valueOf(keys[1]),
                DyeColor.valueOf(keys[2]));
    }

    public static TropicalFishVariant getVariant(TropicalFish fish) {
        return new TropicalFishVariant(fish.getPattern(), fish.getBodyColor(), fish.getPatternColor());
    }

}
