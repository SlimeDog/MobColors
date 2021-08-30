package dev.ratas.mobcolors.config.mob;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

import dev.ratas.mobcolors.config.HorseVariant;
import dev.ratas.mobcolors.config.TropicalFishVariant;

public final class MobTypes {
    public static final Map<EntityType, MobColorEnumProvider> ENTITY_COLOR_ENUMS;
    static {
        Map<EntityType, MobColorEnumProvider> map = new EnumMap<>(EntityType.class);
        map.put(EntityType.AXOLOTL, new MobColorEnumProvider(Axolotl.Variant.class));
        map.put(EntityType.CAT, new MobColorEnumProvider(Cat.Type.class));
        map.put(EntityType.FOX, new MobColorEnumProvider(Fox.Type.class));
        map.put(EntityType.HORSE, new MobColorEnumProvider(HorseVariant.class));
        map.put(EntityType.LLAMA, new MobColorEnumProvider(Llama.Color.class));
        map.put(EntityType.MUSHROOM_COW, new MobColorEnumProvider(MushroomCow.Variant.class));
        map.put(EntityType.PARROT, new MobColorEnumProvider(Parrot.Variant.class));
        map.put(EntityType.RABBIT, new MobColorEnumProvider(Rabbit.Type.class));
        map.put(EntityType.SHEEP, new MobColorEnumProvider(DyeColor.class));
        map.put(EntityType.SHULKER, new MobColorEnumProvider(DyeColor.class));
        map.put(EntityType.TROPICAL_FISH, new MobColorEnumProvider(TropicalFishVariant.class));
        ENTITY_COLOR_ENUMS = Collections.unmodifiableMap(map);
    }
    private static final TranslationLayer TRANSLATION_LAYER = new TranslationLayer();
    private static final EntityTypeTranslationLayer ENTITY_TYPE_TRANSLATION_LAYER = new EntityTypeTranslationLayer();
    public static final List<String> ENTITY_TYPE_NAMES = Collections.unmodifiableList(MobTypes.ENTITY_COLOR_ENUMS
            .keySet().stream().map(type -> type == EntityType.MUSHROOM_COW ? "mooshroom" : type.name().toLowerCase())
            .collect(Collectors.toList()));

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

    public static Function<Entity, ?> getFunctionFor(Entity ent) {
        if (ent instanceof Colorable) {
            return e -> ((Colorable) e).getColor();
        } else if (ent instanceof Axolotl) {
            return e -> ((Axolotl) e).getVariant();
        } else if (ent instanceof Cat) {
            return e -> ((Cat) e).getCatType();
        } else if (ent instanceof Fox) {
            return e -> ((Fox) e).getFoxType();
        } else if (ent instanceof Llama) {
            return e -> ((Llama) e).getColor();
        } else if (ent instanceof MushroomCow) {
            return e -> ((MushroomCow) e).getVariant();
        } else if (ent instanceof Parrot) {
            return e -> ((Parrot) e).getVariant();
        } else if (ent instanceof Rabbit) {
            return e -> ((Rabbit) e).getRabbitType();
        } else if (ent instanceof Horse) {
            return e -> HorseVariant.getVariant((Horse) e);
        } else if (ent instanceof TropicalFish) {
            return e -> TropicalFishVariant.getVariant((TropicalFish) e);
        } else {
            throw new IllegalArgumentException("Not a type of interest: " + ent.getType());
        }
    }

    public static Function<Entity, ?> getFunctionForType(EntityType type) {
        if (type == EntityType.SHEEP || type == EntityType.SHULKER) {
            return e -> ((Colorable) e).getColor();
        } else if (type == EntityType.AXOLOTL) {
            return e -> ((Axolotl) e).getVariant();
        } else if (type == EntityType.CAT) {
            return e -> ((Cat) e).getCatType();
        } else if (type == EntityType.FOX) {
            return e -> ((Fox) e).getFoxType();
        } else if (type == EntityType.LLAMA) {
            return e -> ((Llama) e).getColor();
        } else if (type == EntityType.MUSHROOM_COW) {
            return e -> ((MushroomCow) e).getVariant();
        } else if (type == EntityType.PARROT) {
            return e -> ((Parrot) e).getVariant();
        } else if (type == EntityType.RABBIT) {
            return e -> ((Rabbit) e).getRabbitType();
        } else if (type == EntityType.HORSE) {
            return e -> HorseVariant.getVariant((Horse) e);
        } else if (type == EntityType.TROPICAL_FISH) {
            return e -> TropicalFishVariant.getVariant((TropicalFish) e);
        } else {
            throw new IllegalArgumentException("Not a type of interest: " + type);
        }
    }

    public static String fixTypeNames(String name, Class<?> clazz) {
        return TRANSLATION_LAYER.attemptTranslation(name, clazz);
    }

    public static String reverseTranslate(String name) {
        return TRANSLATION_LAYER.reverseTranslate(name);
    }

    public static String translateEntityTypeName(String name) {
        return ENTITY_TYPE_TRANSLATION_LAYER.attemptTranslation(name);
    }

    public static String reverseTranslateTypeName(EntityType type) {
        return ENTITY_TYPE_TRANSLATION_LAYER.reverseTranslate(type);
    }

    public static final class MobColorEnumProvider {
        private final Class<?> clazz;

        private MobColorEnumProvider(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Class<?> getEnumClass() {
            return clazz;
        }

    }

}
