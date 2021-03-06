package dev.ratas.mobcolors.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import dev.ratas.mobcolors.utils.LogUtils;
import dev.ratas.slimedogcore.api.config.SDCConfiguration;

public class ColorSchemeParser<T> extends AbstractColorSchemeParser {
    private static final double TOLERANCE = 0.0000001D;
    private final Map<T, Double> colorMap;

    public ColorSchemeParser(Class<T> clazz, SDCConfiguration section) {
        super(section);
        colorMap = new HashMap<>();
        SDCConfiguration probabilitiesSection = section.getConfigurationSection("probabilities");
        if (probabilitiesSection == null) {
            throw new IllegalStateException("No probabilities provided");
        }
        Map<T, Double> initialMap = new HashMap<>();
        double total = 0;
        for (String key : probabilitiesSection.getKeys(false)) {
            String variantName = key;
            T color;
            try {
                color = getValueOf(clazz, variantName);
            } catch (IllegalArgumentException e) {
                LogUtils.getLogger().warning("Undefined color in config:" + key);
                continue;
            }
            double val = probabilitiesSection.getDouble(key, 0.0D);
            if (val == 0.0D) {
                continue;
            }
            initialMap.put(color, val);
            total += val;
        }
        if (Math.abs(total - 1.0D) > TOLERANCE) {
            throw new IllegalStateException("Probabilities must add up to 1. Got " + total);
        }
        for (Entry<T, Double> entry : initialMap.entrySet()) {
            colorMap.put(entry.getKey(), entry.getValue() / total);
        }
        if (colorMap.isEmpty()) {
            throw new IllegalArgumentException("No entries found");
        }
    }

    public static <T> T getValueOf(Class<T> clazz, String variantName) {
        return ENUM_VALUE_OF_PROVIDER.getValueOf(clazz, variantName);
    }

    public Map<T, Double> getDyeColors() {
        return colorMap;
    }

}