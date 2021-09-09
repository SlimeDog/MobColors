package dev.ratas.mobcolors.config.variants;

import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Axolotl.Variant;

public enum AxolotlVariant implements MobTypeVariant<Axolotl.Variant> {
    lucy(Axolotl.Variant.LUCY), wild(Axolotl.Variant.WILD), gold(Axolotl.Variant.GOLD), cyan(Axolotl.Variant.CYAN),
    blue(Axolotl.Variant.BLUE);

    private static final ReverseTranslator<Axolotl.Variant, AxolotlVariant> REVERSE_MAP = new ReverseTranslator<>(
            Axolotl.Variant.class);

    private final Axolotl.Variant variant;

    private AxolotlVariant(Axolotl.Variant variant) {
        this.variant = variant;
    }

    @Override
    public Variant getBukkitVariant() {
        return variant;
    }

    public static AxolotlVariant getType(Axolotl.Variant type) {
        if (REVERSE_MAP.isEmpty()) { // initializing here since it's not possible to access static final objects
                                     // on enum constant initialization
            REVERSE_MAP.fill(values());
        }
        return REVERSE_MAP.get(type);
    }

}
