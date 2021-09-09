package dev.ratas.mobcolors.config.variants;

import org.bukkit.DyeColor;

public enum DyeVariant implements MobTypeVariant<DyeColor> {
    white(DyeColor.WHITE), orange(DyeColor.ORANGE), magenta(DyeColor.MAGENTA), light_blue(DyeColor.LIGHT_BLUE),
    yellow(DyeColor.YELLOW), lime(DyeColor.LIME), pink(DyeColor.PINK), gray(DyeColor.GRAY),
    light_gray(DyeColor.LIGHT_GRAY), cyan(DyeColor.CYAN), purple(DyeColor.PURPLE), blue(DyeColor.BLUE),
    brown(DyeColor.BROWN), green(DyeColor.GREEN), red(DyeColor.RED), black(DyeColor.BLACK),
    /**
     * For us within Shulkers - means default shulker
     */
    DEFAULT(null);

    private static final ReverseTranslator<DyeColor, DyeVariant> REVERSE_MAP = new ReverseTranslator<>(DyeColor.class);

    private final DyeColor type;

    private DyeVariant(DyeColor type) {
        this.type = type;
    }

    @Override
    public DyeColor getBukkitVariant() {
        return type;
    }

    public static DyeVariant getType(DyeColor type) {
        if (REVERSE_MAP.isEmpty()) { // initializing here since it's not possible to access static final objects
                                     // on enum constant initialization
            REVERSE_MAP.fill(values());
        }
        return REVERSE_MAP.get(type);
    }

}
