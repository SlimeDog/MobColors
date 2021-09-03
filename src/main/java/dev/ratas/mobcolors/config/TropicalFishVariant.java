package dev.ratas.mobcolors.config;

import java.util.EnumMap;
import java.util.Map;

import org.bukkit.DyeColor;
import org.bukkit.entity.TropicalFish;

import dev.ratas.mobcolors.utils.Triple;

public class TropicalFishVariant extends Triple<TropicalFish.Pattern, DyeColor, DyeColor> {
    private static final InstanceTracker INSTANCE_TRACKER = new InstanceTracker();
    public static final String DELIMITER = "/";

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

    @Override
    public String toString() {
        return getName();
    }

    public static TropicalFishVariant getVariant(TropicalFish fish) {
        return INSTANCE_TRACKER.get(fish.getPattern(), fish.getBodyColor(), fish.getPatternColor());
    }

    private static class InstanceTracker {
        private final Map<TropicalFish.Pattern, Map<DyeColor, Map<DyeColor, TropicalFishVariant>>> variantsMap = new EnumMap<>(
                TropicalFish.Pattern.class);

        private TropicalFishVariant get(TropicalFish.Pattern pattern, DyeColor color1, DyeColor color2) {
            Map<DyeColor, Map<DyeColor, TropicalFishVariant>> map = variantsMap.computeIfAbsent(pattern,
                    c -> new EnumMap<>(DyeColor.class));
            Map<DyeColor, TropicalFishVariant> map2 = map.computeIfAbsent(color1, (c) -> new EnumMap<>(DyeColor.class));
            return map2.computeIfAbsent(color2, c -> new TropicalFishVariant(pattern, color1, color2));
        }
    }

}
