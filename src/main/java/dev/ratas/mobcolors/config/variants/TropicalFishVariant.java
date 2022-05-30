package dev.ratas.mobcolors.config.variants;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.entity.TropicalFish;

public class TropicalFishVariant extends ThreeTypeComplexVariant<TropicalFishPattern, DyeVariant, DyeVariant> {
    private static final String RANDOM_STATE = "random";
    private static final InstanceTracker<TropicalFishPattern, InstanceTracker<DyeVariant, InstanceTracker<DyeVariant, TropicalFishVariant>>> INSTANCE_TRACKER = InstanceTracker
            .getTripleTracker((p, c1, c2) -> new TropicalFishVariant(p, c1, c2));
    public static final String DELIMITER = "/";

    private TropicalFishVariant(TropicalFishPattern pattern, DyeVariant color1, DyeVariant color2) {
        super(pattern, color1, color2);
    }

    public static TropicalFishVariant valueOf(String name) {
        if (name.equals(RANDOM_STATE)) {
            return randomVariant();
        }
        String[] keys = name.split(DELIMITER);
        int nr = keys.length;
        if (nr != 3) {
            throw new IllegalArgumentException(
                    "Wrong number of different types in key '" + name + "'. Expected " + 3 + " but got " + nr);
        }
        return getVariant(TropicalFishPattern.valueOf(keys[0]), DyeVariant.valueOf(keys[1]),
                DyeVariant.valueOf(keys[2]));
    }

    public static TropicalFishVariant randomVariant() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        TropicalFishPattern[] values = TropicalFishPattern.values();
        int nr = random.nextInt(values.length);
        TropicalFishPattern pattern = values[nr];
        DyeVariant[] dyeValues = DyeVariant.values();
        nr = random.nextInt(dyeValues.length);
        DyeVariant color1 = dyeValues[nr];
        nr = random.nextInt(dyeValues.length);
        DyeVariant color2 = dyeValues[nr];
        return getVariant(pattern, color1, color2);
    }

    @Override
    public String toString() {
        return getName();
    }

    public static TropicalFishVariant getVariant(TropicalFish fish) {
        return getVariant(TropicalFishPattern.getType(fish.getPattern()), DyeVariant.getType(fish.getBodyColor()),
                DyeVariant.getType(fish.getPatternColor()));
    }

    public static TropicalFishVariant getVariant(TropicalFishPattern pattern, DyeVariant color1, DyeVariant color2) {
        return INSTANCE_TRACKER.get(pattern).get(color1).get(color2);
    }

}
