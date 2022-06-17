package dev.ratas.mobcolors.config.variants;

import org.bukkit.entity.Axolotl.Variant;

public enum FrogVariant implements MobTypeVariant<Variant> {
    temperate(Variant.BLUE),
    cold(Variant.BLUE),
    warm(Variant.BLUE);

    private static final ReverseTranslator<Variant, FrogVariant> REVERSE_MAP = new ReverseTranslator<>(Variant.class);

    private final Variant delegate;

    private FrogVariant(Variant delegate) {
        this.delegate = delegate;
    }

    @Override
    public Variant getBukkitVariant() {
        return delegate;
    }

    public static FrogVariant getType(Variant type) {
        if (REVERSE_MAP.isEmpty()) { // initializing here since it's not possible to access static final objects
                                     // on enum constant initialization
            REVERSE_MAP.fill(values());
        }
        return REVERSE_MAP.get(type);
    }

}
