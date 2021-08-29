package dev.ratas.mobcolors.config.mob;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import dev.ratas.mobcolors.coloring.settings.AbstractColorMap;
import dev.ratas.mobcolors.coloring.settings.ColorMap;
import dev.ratas.mobcolors.config.ColorSchemeParser;
import dev.ratas.mobcolors.config.mob.MobTypes.MobColorEnumProvider;

public class MobSettingsParser {
    private final ConfigurationSection section;
    private final Logger logger;
    private boolean hasParsed = false;
    private boolean enabled;
    private EntityType type;
    private Map<String, ColorMap<?>> colorMaps;
    private boolean includeLeashed;
    private boolean includePets;

    public MobSettingsParser(ConfigurationSection section, Logger logger) throws IllegalMobSettingsException {
        this.section = section;
        this.logger = logger;
        parse();
    }

    private boolean parseEnabled() {
        return section.getBoolean("enabled");
    }

    private EntityType parseType() throws IllegalMobSettingsException {
        String name = section.getName();
        name = MobTypes.translateEntityTypeName(name);
        try {
            return EntityType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalMobSettingsException("Illegal entity type specified: " + name);
        }
    }

    private Map<String, ColorMap<?>> parseMaps() {
        Map<String, ColorMap<?>> map = new HashMap<>();
        MobColorEnumProvider provider = MobTypes.ENTITY_COLOR_ENUMS.get(type);
        if (provider == null) {
            throw new IllegalMobSettingsException("Entity of " + type + " type does not have any changes configured");
        }
        Class<?> clazz = provider.getEnumClass();
        for (String name : section.getKeys(false)) {
            if (!section.isConfigurationSection(name)) {
                continue; // ignore non-sections such as 'enabled', 'include-leashed' and 'include-pets'
            }
            ColorSchemeParser<?> parser;
            try {
                parser = new ColorSchemeParser<>(clazz, section.getConfigurationSection(name), logger);
            } catch (IllegalStateException e) {
                logger.warning("Unable to parse color map " + name + " for " + type + ": " + e.getMessage());
                continue;
            }
            ColorMap<?> colorMap = new AbstractColorMap<>(type, parser.getName(), parser.getDyeColors(),
                    parser.getEnabledWorlds());
            map.put(name, colorMap);
        }
        return map;
    }

    private boolean parseIncludeLeashed() {
        return section.getBoolean("include-leashed");
    }

    private boolean parseIncludePets() {
        return section.getBoolean("include-pets");
    }

    public void parse() throws IllegalMobSettingsException {
        enabled = parseEnabled();
        if (enabled) { // only parse the rest if needed
            type = parseType();
            colorMaps = parseMaps();
            includeLeashed = parseIncludeLeashed();
            includePets = parseIncludePets();
        }
        hasParsed = true;
    }

    public boolean isValid() {
        return (hasParsed && !enabled) || (type != null && colorMaps != null);
    }

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Gets the MobSettings instance or null if not enabled.
     *
     * @return
     */
    public MobSettings get() {
        if (!isValid()) {
            throw new IllegalStateException("Invalid state of mob settings parser");
        }
        if (!enabled) {
            return null;
        }
        return new MobSettings(type, colorMaps, includeLeashed, includePets);
    }

}
