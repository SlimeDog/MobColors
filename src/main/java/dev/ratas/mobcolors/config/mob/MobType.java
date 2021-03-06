package dev.ratas.mobcolors.config.mob;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import org.bukkit.entity.EntityType;

import dev.ratas.mobcolors.config.HorseVariant;
import dev.ratas.mobcolors.config.variants.AxolotlVariant;
import dev.ratas.mobcolors.config.variants.CatVariant;
import dev.ratas.mobcolors.config.variants.DyeVariant;
import dev.ratas.mobcolors.config.variants.FoxVariant;
import dev.ratas.mobcolors.config.variants.FrogVariant;
import dev.ratas.mobcolors.config.variants.LlamaVariant;
import dev.ratas.mobcolors.config.variants.MooshroomVariant;
import dev.ratas.mobcolors.config.variants.ParrotVariant;
import dev.ratas.mobcolors.config.variants.RabbitVariant;
import dev.ratas.mobcolors.config.variants.TropicalFishVariant;

public enum MobType {
    axolotl("AXOLOTL", AxolotlVariant.class), cat(EntityType.CAT, CatVariant.class),
    fox(EntityType.FOX, FoxVariant.class), horse(EntityType.HORSE, HorseVariant.class),
    llama(EntityType.LLAMA, LlamaVariant.class), mooshroom(EntityType.MUSHROOM_COW, MooshroomVariant.class),
    parrot(EntityType.PARROT, ParrotVariant.class), rabbit(EntityType.RABBIT, RabbitVariant.class),
    sheep(EntityType.SHEEP, DyeVariant.class), shulker(EntityType.SHULKER, DyeVariant.class),
    tropical_fish(EntityType.TROPICAL_FISH, TropicalFishVariant.class), frog("FROG", FrogVariant.class);

    private static final Map<EntityType, MobType> REVERSE_MAP = new EnumMap<>(EntityType.class);
    private static final MobType[] AVAILABLE_VALUES = getAvailableValues();

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

    public static MobType[] availableValues() {
        return Arrays.copyOf(AVAILABLE_VALUES, AVAILABLE_VALUES.length);
    }

    private static MobType[] getAvailableValues() {
        MobType[] enums = values();
        int existing = 0;
        for (MobType type : enums) {
            if (type.isValid()) {
                existing++;
            }
        }
        MobType[] types = new MobType[existing];
        int i = 0;
        for (MobType type : enums) {
            if (type.isValid()) {
                types[i++] = type;
            }
        }
        return types;
    }

}
