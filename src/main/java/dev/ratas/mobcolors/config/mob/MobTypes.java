package dev.ratas.mobcolors.config.mob;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import org.bukkit.DyeColor;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.TropicalFish;
import org.bukkit.material.Colorable;

public final class MobTypes {
    public static final Map<EntityType, MobColorEnumProvider> ENTITY_COLOR_ENUMS;
    static {
        Map<EntityType, MobColorEnumProvider> map = new EnumMap<>(EntityType.class);
        map.put(EntityType.AXOLOTL, new MobColorEnumProvider(Axolotl.Variant.class));
        map.put(EntityType.CAT, new MobColorEnumProvider(Cat.Type.class));
        map.put(EntityType.FOX, new MobColorEnumProvider(Fox.Type.class));
        map.put(EntityType.HORSE, new MobColorEnumProvider(Horse.Color.class, Horse.Style.class));
        map.put(EntityType.LLAMA, new MobColorEnumProvider(Llama.Color.class));
        map.put(EntityType.MUSHROOM_COW, new MobColorEnumProvider(MushroomCow.Variant.class));
        map.put(EntityType.PARROT, new MobColorEnumProvider(Parrot.Variant.class));
        map.put(EntityType.RABBIT, new MobColorEnumProvider(Rabbit.Type.class));
        map.put(EntityType.SHEEP, new MobColorEnumProvider(DyeColor.class));
        map.put(EntityType.SHULKER, new MobColorEnumProvider(DyeColor.class));
        map.put(EntityType.TROPICAL_FISH, new MobColorEnumProvider(TropicalFish.Pattern.class, DyeColor.class));
        ENTITY_COLOR_ENUMS = Collections.unmodifiableMap(map);
    }

    private MobTypes() {
        throw new IllegalStateException("Cannot be initialized");
    }

    public static Class<?> getInterestingClass(Entity ent) {
        if (ent instanceof Colorable) {
            return Colorable.class;
        } else if (ent instanceof Axolotl) {
            return Axolotl.class;
        } else if (ent instanceof Cat) {
            return Cat.class;
        } else if (ent instanceof Fox) {
            return Fox.class;
        } else if (ent instanceof Llama) {
            return Llama.class;
        } else if (ent instanceof MushroomCow) {
            return MushroomCow.class;
        } else if (ent instanceof Parrot) {
            return Parrot.class;
        } else if (ent instanceof Rabbit) {
            return Rabbit.class;
        } else if (ent instanceof Horse) {
            return Horse.class;
        } else if (ent instanceof TropicalFish) {
            return TropicalFish.class;
        } else {
            return null;
        }
    }

    public static final class MobColorEnumProvider {
        private final Class<? extends Enum<?>> clazz1;
        private final Class<? extends Enum<?>> clazz2;

        private MobColorEnumProvider(Class<? extends Enum<?>> clazz1) {
            this(clazz1, null);
        }

        private MobColorEnumProvider(Class<? extends Enum<?>> clazz1, Class<? extends Enum<?>> clazz2) {
            this.clazz1 = clazz1;
            this.clazz2 = clazz2;
        }

        public boolean hasTwoEnums() {
            return clazz2 != null;
        }

        public Class<? extends Enum<?>> getEnumClass() {
            if (clazz2 != null) {
                throw new IllegalStateException(
                        "This enum provider has two classes and one must be specifically asked for");
            }
            return clazz1;
        }

        public Class<? extends Enum<?>> getFirstEnumClass() {
            return clazz1;
        }

        public Class<? extends Enum<?>> getSecondEnumClass() {
            return clazz2;
        }

    }

}
