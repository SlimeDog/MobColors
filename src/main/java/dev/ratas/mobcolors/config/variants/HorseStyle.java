package dev.ratas.mobcolors.config.variants;

import org.bukkit.entity.Horse;

public enum HorseStyle implements MobTypeVariant<Horse.Style> {
    none(Horse.Style.NONE), white(Horse.Style.WHITE), whitefield(Horse.Style.WHITEFIELD),
    white_dots(Horse.Style.WHITE_DOTS), black_dots(Horse.Style.BLACK_DOTS);

    private static final ReverseTranslator<Horse.Style, HorseStyle> REVERSE_MAP = new ReverseTranslator<>(
            Horse.Style.class);

    private final Horse.Style color;

    private HorseStyle(Horse.Style color) {
        this.color = color;
    }

    @Override
    public Horse.Style getBukkitVariant() {
        return color;
    }

    public static HorseStyle getType(Horse.Style type) {
        if (REVERSE_MAP.isEmpty()) { // initializing here since it's not possible to access static final objects
                                     // on enum constant initialization
            REVERSE_MAP.fill(values());
        }
        return REVERSE_MAP.get(type);
    }
}
