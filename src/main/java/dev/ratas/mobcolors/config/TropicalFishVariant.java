package dev.ratas.mobcolors.config;

import org.bukkit.DyeColor;
import org.bukkit.entity.TropicalFish;

import dev.ratas.mobcolors.config.variants.ThreeTypeComplexVariant;

public class TropicalFishVariant extends ThreeTypeComplexVariant<TropicalFish.Pattern, DyeColor, DyeColor> {
    private static final InstanceTracker<TropicalFish.Pattern, InstanceTracker<DyeColor, InstanceTracker<DyeColor, TropicalFishVariant>>> INSTANCE_TRACKER = InstanceTracker
            .getTripleTracker((p, c1, c2) -> new TropicalFishVariant(p, c1, c2));
    public static final String DELIMITER = "/";

    private TropicalFishVariant(TropicalFish.Pattern pattern, DyeColor color1, DyeColor color2) {
        super(pattern, color1, color2);
    }

    public static TropicalFishVariant valueOf(String name) {
        String[] keys = name.split(DELIMITER);
        int nr = keys.length;
        if (nr != 3) {
            throw new IllegalArgumentException(
                    "Wrong number of different types in key '" + name + "'. Expected " + 3 + " but got " + nr);
        }
        return getVariant(TropicalFish.Pattern.valueOf(keys[0]), DyeColor.valueOf(keys[1]), DyeColor.valueOf(keys[2]));
    }

    @Override
    public String toString() {
        return getName();
    }

    public static TropicalFishVariant getVariant(TropicalFish fish) {
        return getVariant(fish.getPattern(), fish.getBodyColor(), fish.getPatternColor());
    }

    public static TropicalFishVariant getVariant(TropicalFish.Pattern pattern, DyeColor color1, DyeColor color2) {
        return INSTANCE_TRACKER.get(pattern).get(color1).get(color2);
    }

}
