package dev.ratas.mobcolors.config.variants;

import org.bukkit.entity.Wolf;

public enum WolfVariant implements MobTypeVariant<Wolf.Variant> {
    pale(Wolf.Variant.PALE),
    spotted(Wolf.Variant.SPOTTED),
    snowy(Wolf.Variant.SNOWY),
    black(Wolf.Variant.BLACK),
    ashen(Wolf.Variant.ASHEN),
    rusty(Wolf.Variant.RUSTY),
    woods(Wolf.Variant.WOODS),
    chestnut(Wolf.Variant.CHESTNUT),
    striped(Wolf.Variant.STRIPED);

    private static final ReverseTranslator<Wolf.Variant, WolfVariant> REVERSE_MAP = new ReverseTranslator<>(Wolf.Variant.class);

    private final Wolf.Variant type;

    private WolfVariant(Wolf.Variant type) {
        this.type = type;
    }

    @Override
    public Wolf.Variant getBukkitVariant() {
        return type;
    }

    public static WolfVariant getVariant(Wolf.Variant type) {
        if (REVERSE_MAP.isEmpty()) { // initializing here since it's not possible to access static final objects
                                     // on enum constant initialization
            REVERSE_MAP.fill(values());
        }
        return REVERSE_MAP.get(type);
    }

}
