package dev.ratas.mobcolors.config.variants;

import org.bukkit.entity.Cat;
import org.bukkit.entity.Cat.Type;

public enum CatVariant implements MobTypeVariant<Cat.Type> {
    tabby(Cat.Type.TABBY), black(Cat.Type.BLACK), red(Cat.Type.RED), siamese(Cat.Type.SIAMESE),
    british_shorthair(Cat.Type.BRITISH_SHORTHAIR), calico(Cat.Type.CALICO), persian(Cat.Type.PERSIAN),
    ragdoll(Cat.Type.RAGDOLL), white(Cat.Type.WHITE), jellie(Cat.Type.JELLIE), all_black(Cat.Type.ALL_BLACK);

    private static final ReverseTranslator<Cat.Type, CatVariant> REVERSE_MAP = new ReverseTranslator<>(Cat.Type.class);

    private final Cat.Type type;

    private CatVariant(Cat.Type type) {
        this.type = type;
    }

    @Override
    public Type getBukkitVariant() {
        return type;
    }

    public static CatVariant getType(Cat.Type type) {
        if (REVERSE_MAP.isEmpty()) { // initializing here since it's not possible to access static final objects
                                     // on enum constant initialization
            REVERSE_MAP.fill(values());
        }
        return REVERSE_MAP.get(type);
    }

}
