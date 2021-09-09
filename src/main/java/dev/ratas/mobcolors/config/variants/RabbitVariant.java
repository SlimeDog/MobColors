package dev.ratas.mobcolors.config.variants;

import org.bukkit.entity.Rabbit;

public enum RabbitVariant implements MobTypeVariant<Rabbit.Type> {
    brown(Rabbit.Type.BROWN), white(Rabbit.Type.WHITE), black(Rabbit.Type.BLACK),
    black_and_white(Rabbit.Type.BLACK_AND_WHITE), gold(Rabbit.Type.GOLD), salt_and_pepper(Rabbit.Type.SALT_AND_PEPPER),
    the_killer_bunny(Rabbit.Type.THE_KILLER_BUNNY);

    private static final ReverseTranslator<Rabbit.Type, RabbitVariant> REVERSE_MAP = new ReverseTranslator<>(
            Rabbit.Type.class);

    private final Rabbit.Type type;

    private RabbitVariant(Rabbit.Type type) {
        this.type = type;
    }

    @Override
    public Rabbit.Type getBukkitVariant() {
        return type;
    }

    public static RabbitVariant getType(Rabbit.Type type) {
        if (REVERSE_MAP.isEmpty()) { // initializing here since it's not possible to access static final objects
                                     // on enum constant initialization
            REVERSE_MAP.fill(values());
        }
        return REVERSE_MAP.get(type);
    }

}
