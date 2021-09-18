package dev.ratas.mobcolors.config.mob;

import java.util.EnumMap;
import java.util.Map;

import org.bukkit.entity.EntityType;

import dev.ratas.mobcolors.config.HorseVariant;
import dev.ratas.mobcolors.config.TropicalFishVariant;
import dev.ratas.mobcolors.config.variants.AxolotlVariant;
import dev.ratas.mobcolors.config.variants.CatVariant;
import dev.ratas.mobcolors.config.variants.DyeVariant;
import dev.ratas.mobcolors.config.variants.FoxVariant;
import dev.ratas.mobcolors.config.variants.LlamaVariant;
import dev.ratas.mobcolors.config.variants.MooshroomVariant;
import dev.ratas.mobcolors.config.variants.ParrotVariant;
import dev.ratas.mobcolors.config.variants.RabbitVariant;

public enum MobType {
    axolotl("AXOLOTL", AxolotlVariant.class), cat(EntityType.CAT, CatVariant.class),
    fox(EntityType.FOX, FoxVariant.class), horse(EntityType.HORSE, HorseVariant.class),
    llama(EntityType.LLAMA, LlamaVariant.class), mooshroom(EntityType.MUSHROOM_COW, MooshroomVariant.class),
    parrot(EntityType.PARROT, ParrotVariant.class), rabbit(EntityType.RABBIT, RabbitVariant.class),
    sheep(EntityType.SHEEP, DyeVariant.class), shulker(EntityType.SHULKER, DyeVariant.class),
    tropical_fish(EntityType.TROPICAL_FISH, TropicalFishVariant.class);

    private static final Map<EntityType, MobType> REVERSE_MAP = new EnumMap<>(EntityType.class);

    private final EntityType delegate;
    private final Class<?> typeClass;

    private MobType(String enumName, Class<?> typeClass) {
        EntityType type;
        try {
            type = EntityType.valueOf(enumName);
        } catch (IllegalArgumentException e) {
            type = null;
        }
        this.delegate = type;
        this.typeClass = typeClass;
    }

    private MobType(EntityType delegate, Class<?> typeClass) {
        this.delegate = delegate;
        this.typeClass = typeClass;
    }

    public boolean isValid() {
        return delegate != null;
    }

    public EntityType getEntityType() {
        return delegate;
    }

    public Class<?> getTypeClass() {
        return typeClass;
    }

    // filling here since I can't access static and final variables during
    // initialization
    private static void fillReverseMap() {
        for (MobType type : values()) {
            if (type.delegate == null) {
                continue; // unsupported mob types
            }
            REVERSE_MAP.put(type.delegate, type);
        }
    }

    public static MobType getType(EntityType type) {
        if (REVERSE_MAP.isEmpty()) {
            fillReverseMap();
        }
        if (type == EntityType.TRADER_LLAMA) {
            type = EntityType.LLAMA;
        }
        return REVERSE_MAP.get(type);
    }

}
