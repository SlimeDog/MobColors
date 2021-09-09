package dev.ratas.mobcolors.config.variants;

import org.bukkit.entity.Parrot;
import org.bukkit.entity.Parrot.Variant;

public enum ParrotVariant implements MobTypeVariant<Parrot.Variant> {
    red(Parrot.Variant.RED), blue(Parrot.Variant.BLUE), green(Parrot.Variant.GREEN), cyan(Parrot.Variant.CYAN),
    gray(Parrot.Variant.GRAY);

    private static final ReverseTranslator<Parrot.Variant, ParrotVariant> REVERSE_MAP = new ReverseTranslator<>(
            Parrot.Variant.class);

    private final Parrot.Variant variant;

    private ParrotVariant(Parrot.Variant variant) {
        this.variant = variant;
    }

    @Override
    public Variant getBukkitVariant() {
        return variant;
    }

    public static ParrotVariant getType(Parrot.Variant type) {
        if (REVERSE_MAP.isEmpty()) { // initializing here since it's not possible to access static final objects
                                     // on enum constant initialization
            REVERSE_MAP.fill(values());
        }
        return REVERSE_MAP.get(type);
    }

}
