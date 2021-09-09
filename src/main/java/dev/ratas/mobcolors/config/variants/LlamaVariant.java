package dev.ratas.mobcolors.config.variants;

import org.bukkit.entity.Llama;

public enum LlamaVariant implements MobTypeVariant<Llama.Color> {
    cream(Llama.Color.CREAMY), white(Llama.Color.WHITE), brown(Llama.Color.BROWN), gray(Llama.Color.GRAY);

    private static final ReverseTranslator<Llama.Color, LlamaVariant> REVERSE_MAP = new ReverseTranslator<>(
            Llama.Color.class);

    private final Llama.Color color;

    private LlamaVariant(Llama.Color color) {
        this.color = color;
    }

    @Override
    public Llama.Color getBukkitVariant() {
        return color;
    }

    public static LlamaVariant getType(Llama.Color type) {
        if (REVERSE_MAP.isEmpty()) { // initializing here since it's not possible to access static final objects
                                     // on enum constant initialization
            REVERSE_MAP.fill(values());
        }
        return REVERSE_MAP.get(type);
    }

}
