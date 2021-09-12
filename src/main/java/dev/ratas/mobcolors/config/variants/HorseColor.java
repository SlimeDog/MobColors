package dev.ratas.mobcolors.config.variants;

import org.bukkit.entity.Horse;

public enum HorseColor implements MobTypeVariant<Horse.Color> {
    white(Horse.Color.WHITE), creamy(Horse.Color.CREAMY), chestnut(Horse.Color.CHESTNUT), brown(Horse.Color.BROWN),
    black(Horse.Color.BLACK), gray(Horse.Color.GRAY), dark_brown(Horse.Color.DARK_BROWN);

    private static final ReverseTranslator<Horse.Color, HorseColor> REVERSE_MAP = new ReverseTranslator<>(
            Horse.Color.class);

    private final Horse.Color color;

    private HorseColor(Horse.Color color) {
        this.color = color;
    }

    @Override
    public Horse.Color getBukkitVariant() {
        return color;
    }

    public static HorseColor getType(Horse.Color type) {
        if (REVERSE_MAP.isEmpty()) { // initializing here since it's not possible to access static final objects
                                     // on enum constant initialization
            REVERSE_MAP.fill(values());
        }
        return REVERSE_MAP.get(type);
    }

}
