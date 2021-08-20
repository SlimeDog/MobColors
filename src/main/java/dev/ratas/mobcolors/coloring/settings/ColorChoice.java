package dev.ratas.mobcolors.coloring.settings;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ColorChoice<T> {
    private final T choice;
    private final double chance;

    public ColorChoice(T choice, double chance) {
        this.choice = choice;
        this.chance = chance;
    }

    public T getColor() {
        return choice;
    }

    public double getChance() {
        return chance;
    }

    public static <V> ColorChoice<V> of(ColorMap<V> map) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        double nr = random.nextDouble();
        double cur = 0;
        V last = null;
        for (Map.Entry<V, Double> entry : map.getChoices().entrySet()) { // these should add up to 1
            cur += entry.getValue();
            if (nr <= cur) {
                return new ColorChoice<>(entry.getKey(), nr);
            }
            last = entry.getKey();
        }
        return new ColorChoice<>(last, nr);
    }
    
}
