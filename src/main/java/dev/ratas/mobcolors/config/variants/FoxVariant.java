package dev.ratas.mobcolors.config.variants;

import org.bukkit.entity.Fox;

public enum FoxVariant implements MobTypeVariant<Fox.Type> {
    red(Fox.Type.RED), snow(Fox.Type.SNOW);

    private static final ReverseTranslator<Fox.Type, FoxVariant> REVERSE_MAP = new ReverseTranslator<>(Fox.Type.class);

    private final Fox.Type type;

    private FoxVariant(Fox.Type type) {
        this.type = type;
    }

    @Override
    public Fox.Type getBukkitVariant() {
        return type;
    }

    public static FoxVariant getType(Fox.Type type) {
        if (REVERSE_MAP.isEmpty()) { // initializing here since it's not possible to access static final objects
                                     // on enum constant initialization
            REVERSE_MAP.fill(values());
        }
        return REVERSE_MAP.get(type);
    }

}
