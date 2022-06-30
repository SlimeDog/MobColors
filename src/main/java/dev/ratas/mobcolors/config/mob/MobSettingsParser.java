package dev.ratas.mobcolors.config.mob;

import java.util.HashMap;
import java.util.Map;

import dev.ratas.mobcolors.coloring.settings.AbstractColorMap;
import dev.ratas.mobcolors.coloring.settings.ColorMap;
import dev.ratas.mobcolors.config.ColorSchemeParser;
import dev.ratas.mobcolors.config.mob.IllegalMobSettingsException.MobTypeNotAvailableException;
import dev.ratas.mobcolors.utils.LogUtils;
import dev.ratas.slimedogcore.api.config.SDCConfiguration;

public class MobSettingsParser {
    private final SDCConfiguration section;
    private boolean hasParsed = false;
    private boolean enabled;
    private MobType type;
    private Map<String, ColorMap<?>> colorMaps;
    private boolean includeLeashed;
    private boolean includePets;
    private boolean includeTraders;
    private final boolean forceEnable;

    public MobSettingsParser(SDCConfiguration section, boolean forceEnable) throws IllegalMobSettingsException {
        this.section = section;
        this.forceEnable = forceEnable;
        parse();
    }

    private boolean parseEnabled() {
        return section.getBoolean("enabled") || this.forceEnable;
    }

    private MobType parseType() throws IllegalMobSettingsException {
        String name = section.getName();
        MobType type;
        try {
            type = MobType.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new IllegalMobSettingsException("Illegal entity type specified: " + name);
        }
        if (!type.isValid()) {
            throw new MobTypeNotAvailableException(type.name());
        }
        return type;
    }

    private Map<String, ColorMap<?>> parseMaps() {
        Map<String, ColorMap<?>> map = new HashMap<>();
        Class<?> clazz = type.getTypeClass();
        for (String name : section.getKeys(false)) {
            if (!section.isConfigurationSection(name)) {
                continue; // ignore non-sections such as 'enabled', 'include-leashed' and 'include-pets'
            }
            ColorSchemeParser<?> parser;
            try {
                parser = new ColorSchemeParser<>(clazz, section.getConfigurationSection(name));
            } catch (IllegalStateException e) {
                LogUtils.getLogger()
                        .warning("Unable to parse color map " + name + " for " + type + ": " + e.getMessage());
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

    private boolean parseIncludeTraders() {
        return section.getBoolean("include-traders");
    }

    public void parse() throws IllegalMobSettingsException {
        enabled = parseEnabled();
        if (enabled) { // only parse the rest if needed
            type = parseType();
            colorMaps = parseMaps();
            includeLeashed = parseIncludeLeashed();
            includePets = parseIncludePets();
            if (type == MobType.llama) {
                includeTraders = parseIncludeTraders();
            }
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
        return new MobSettings(type, colorMaps, includeLeashed, includePets, includeTraders);
    }

}
