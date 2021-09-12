package dev.ratas.mobcolors.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;

public class AbstractColorSchemeParser {
    protected static final EnumValueOfProvider ENUM_VALUE_OF_PROVIDER = new EnumValueOfProvider();
    private final String name;
    private final List<String> enabledWorlds = new ArrayList<>();

    protected AbstractColorSchemeParser(ConfigurationSection section, Logger logger) {
        if (section == null) {
            throw new IllegalStateException("Color scheme settings cannot have an empty section");
        }
        this.name = section.getName();
        for (String worldName : section.getStringList("enabled-worlds")) {
            enabledWorlds.add(worldName.toLowerCase());
        }
        if (name.equals("default") && !enabledWorlds.isEmpty()) {
            throw new IllegalStateException("Cannot set worlds to default color map");
        }
    }

    public String getName() {
        return name;
    }

    public List<String> getEnabledWorlds() {
        return enabledWorlds;
    }

    protected static final class EnumValueOfProvider {
        private final Map<Class<?>, Method> methodsPerEnum = new HashMap<>();

        @SuppressWarnings("unchecked")
        public <V> V getValueOf(Class<V> clazz, String name) {
            name = name.equals("default") ? "DEFAULT" : name;
            Method method = getOrCreateValueOfMethod(clazz);
            try {
                return (V) method.invoke(clazz, name); // unchecked, but for Enum should be fine
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException("No such " + clazz.getSimpleName() + ": " + name);
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
