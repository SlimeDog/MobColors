package dev.ratas.mobcolors.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;

public class ColorSchemeParser<T extends Enum<?>> {
    private static final EnumValueOfProvider ENUM_VALUE_OF_PROVIDER = new EnumValueOfProvider();
    private static final double TOLERANCE = 0.0000001D;
    private final String name;
    private final Map<T, Double> colorMap;
    private final List<String> enabledWorlds = new ArrayList<>();

    public ColorSchemeParser(Class<T> clazz, ConfigurationSection section, Logger logger) {
        colorMap = new HashMap<>();
        if (section == null) {
            throw new IllegalStateException("Color scheme settings cannot have an empty section");
        }
        this.name = section.getName();
        ConfigurationSection probabilitiesSection = section.getConfigurationSection("probabilities");
        if (probabilitiesSection == null) {
            throw new IllegalStateException("No probabilities provided");
        }
        Map<T, Double> initialMap = new HashMap<>();
        double total = 0;
        for (String key : probabilitiesSection.getKeys(false)) {
            T color;
            try {
                color = ENUM_VALUE_OF_PROVIDER.getValueOf(clazz, key.toUpperCase());
            } catch (IllegalArgumentException e) {
                logger.warning("Undefined color in config:" + key);
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
        for (String worldName : section.getStringList("enabled-worlds")) {
            enabledWorlds.add(worldName.toLowerCase());
        }
    }

    public String getName() {
        return name;
    }

    public Map<T, Double> getDyeColors() {
        return colorMap;
    }

    public List<String> getEnabledWorlds() {
        return enabledWorlds;
    }

    private static final class EnumValueOfProvider {
        private final Map<Class<?>, Method> methodsPerEnum = new HashMap<>();

        @SuppressWarnings("unchecked")
        public <V extends Enum<?>> V getValueOf(Class<V> clazz, String name) {
            Method method = getOrCreateValueOfMethod(clazz);
            try {
                return (V) method.invoke(clazz, name); // unchecked, but for Enum should be fine
            } catch (IllegalArgumentException e) {
                throw e; // cought above
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException(e);
            }
        }

        private Method getOrCreateValueOfMethod(Class<?> clazz) {
            methodsPerEnum.computeIfAbsent(clazz, (key) -> computeValueOfMethod(key));
            return methodsPerEnum.get(clazz);
        }

        private Method computeValueOfMethod(Class<?> clazz) {
            Method method;
            try {
                method = clazz.getMethod("valueOf", String.class);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException(e);
            }
            return method;
        }

    }

}