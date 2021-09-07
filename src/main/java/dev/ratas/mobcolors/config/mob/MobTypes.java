package dev.ratas.mobcolors.config.mob;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private static final TranslationLayer TRANSLATION_LAYER = new TranslationLayer();
    private static final EntityTypeTranslationLayer ENTITY_TYPE_TRANSLATION_LAYER = new EntityTypeTranslationLayer();
    public static final List<String> ENTITY_TYPE_NAMES = Collections
            .unmodifiableList(Arrays.stream(MobType.values()).map(Enum::name).collect(Collectors.toList()));

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

    public static Function<Entity, ?> getFunctionForType(MobType type) {
        if (type == MobType.sheep || type == MobType.sheep) {
            return e -> ((Colorable) e).getColor();
        } else if (type == MobType.axolotl) {
            return e -> ((Axolotl) e).getVariant();
        } else if (type == MobType.cat) {
            return e -> ((Cat) e).getCatType();
        } else if (type == MobType.fox) {
            return e -> ((Fox) e).getFoxType();
        } else if (type == MobType.llama) {
            return e -> ((Llama) e).getColor();
        } else if (type == MobType.mooshroom) {
            return e -> ((MushroomCow) e).getVariant();
        } else if (type == MobType.parrot) {
            return e -> ((Parrot) e).getVariant();
        } else if (type == MobType.rabbit) {
            return e -> ((Rabbit) e).getRabbitType();
        } else if (type == MobType.horse) {
            return e -> HorseVariant.getVariant((Horse) e);
        } else if (type == MobType.tropical_fish) {
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

}
