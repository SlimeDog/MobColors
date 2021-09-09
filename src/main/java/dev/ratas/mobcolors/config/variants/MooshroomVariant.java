package dev.ratas.mobcolors.config.variants;

import org.bukkit.entity.MushroomCow;

public enum MooshroomVariant implements MobTypeVariant<MushroomCow.Variant> {
    red(MushroomCow.Variant.RED), brown(MushroomCow.Variant.BROWN);

    private static final ReverseTranslator<MushroomCow.Variant, MooshroomVariant> REVERSE_MAP = new ReverseTranslator<>(
            MushroomCow.Variant.class);

    private final MushroomCow.Variant variant;

    private MooshroomVariant(MushroomCow.Variant variant) {
        this.variant = variant;
    }

    @Override
    public MushroomCow.Variant getBukkitVariant() {
        return variant;
    }

    public static MooshroomVariant getType(MushroomCow.Variant type) {
        if (REVERSE_MAP.isEmpty()) { // initializing here since it's not possible to access static final objects
                                     // on enum constant initialization
            REVERSE_MAP.fill(values());
        }
        return REVERSE_MAP.get(type);
    }

}
