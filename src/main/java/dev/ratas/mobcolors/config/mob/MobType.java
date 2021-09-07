package dev.ratas.mobcolors.config.mob;

import java.util.EnumMap;
import java.util.Map;

import org.bukkit.DyeColor;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Cat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Llama;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Rabbit;

import dev.ratas.mobcolors.config.HorseVariant;
import dev.ratas.mobcolors.config.TropicalFishVariant;

public enum MobType {
    axolotl(EntityType.AXOLOTL, Axolotl.Variant.class), cat(EntityType.CAT, Cat.Type.class),
    fox(EntityType.FOX, Fox.Type.class), horse(EntityType.HORSE, HorseVariant.class),
    llama(EntityType.LLAMA, Llama.Color.class), mooshroom(EntityType.MUSHROOM_COW, MushroomCow.Variant.class),
    parrot(EntityType.PARROT, Parrot.Variant.class), rabbit(EntityType.RABBIT, Rabbit.Type.class),
    sheep(EntityType.SHEEP, DyeColor.class), shulker(EntityType.SHULKER, DyeColor.class),
    tropical_fish(EntityType.TROPICAL_FISH, TropicalFishVariant.class);

    private static final Map<EntityType, MobType> REVERSE_MAP = new EnumMap<>(EntityType.class);

    private final EntityType delegate;
    private final Class<?> typeClass;

    private MobType(EntityType delegate, Class<?> typeClass) {
        this.delegate = delegate;
        this.typeClass = typeClass;
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
