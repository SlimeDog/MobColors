package dev.ratas.mobcolors.config.mob;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Frog;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.TropicalFish;
import org.bukkit.material.Colorable;

import dev.ratas.mobcolors.config.HorseVariant;
import dev.ratas.mobcolors.config.variants.AxolotlVariant;
import dev.ratas.mobcolors.config.variants.CatVariant;
import dev.ratas.mobcolors.config.variants.DyeVariant;
import dev.ratas.mobcolors.config.variants.FoxVariant;
import dev.ratas.mobcolors.config.variants.FrogVariant;
import dev.ratas.mobcolors.config.variants.LlamaVariant;
import dev.ratas.mobcolors.config.variants.MobTypeVariant;
import dev.ratas.mobcolors.config.variants.MooshroomVariant;
import dev.ratas.mobcolors.config.variants.ParrotVariant;
import dev.ratas.mobcolors.config.variants.RabbitVariant;
import dev.ratas.mobcolors.config.variants.TropicalFishVariant;

public final class MobTypes {
    // for 1.16.5 support
    private static final Class<? extends Entity> AXOLOTL_CLASS;
    public static final List<String> ENTITY_TYPE_NAMES = Collections.unmodifiableList(
            Arrays.stream(MobType.availableValues()).map(Enum::name).sorted().collect(Collectors.toList()));
    public static final Map<Class<? extends MobTypeVariant<?>>, Set<MobType>> VARIANT_MOB_TYPES;
    static {
        Map<Class<? extends MobTypeVariant<?>>, Set<MobType>> map = new HashMap<>();
        map.put(DyeVariant.class, Collections.unmodifiableSet(EnumSet.of(MobType.sheep, MobType.shulker)));
        map.put(CatVariant.class, Collections.unmodifiableSet(EnumSet.of(MobType.cat)));
        map.put(AxolotlVariant.class, Collections.unmodifiableSet(EnumSet.of(MobType.axolotl)));
        map.put(FoxVariant.class, Collections.unmodifiableSet(EnumSet.of(MobType.fox)));
        map.put(LlamaVariant.class, Collections.unmodifiableSet(EnumSet.of(MobType.llama)));
        map.put(MooshroomVariant.class, Collections.unmodifiableSet(EnumSet.of(MobType.mooshroom)));
        map.put(ParrotVariant.class, Collections.unmodifiableSet(EnumSet.of(MobType.parrot)));
        map.put(RabbitVariant.class, Collections.unmodifiableSet(EnumSet.of(MobType.rabbit)));
        VARIANT_MOB_TYPES = Collections.unmodifiableMap(map);
        Class<? extends Entity> clazz;
        try {
            @SuppressWarnings("unchecked")
            Class<? extends Entity> clazz1 = (Class<? extends Entity>) Class.forName("org.bukkit.entity.Axolotl");
            clazz = clazz1;
        } catch (ClassNotFoundException e) {
            clazz = null;
        }
        AXOLOTL_CLASS = clazz;
    }

    private MobTypes() {
        throw new IllegalStateException("Cannot be initialized");
    }

    public static Class<?> getInterestingClass(Entity ent) {
        if (ent instanceof Colorable) {
            return Colorable.class;
        } else if (AXOLOTL_CLASS != null && AXOLOTL_CLASS.isAssignableFrom(ent.getClass())) {
            return AXOLOTL_CLASS;
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
        } else if (ent instanceof Frog) {
            return FrogVariant.class;
        } else {
            return null;
        }
    }

    public static Function<Entity, ?> getFunctionFor(Entity ent) {
        if (ent instanceof Colorable) {
            return e -> DyeVariant.getType(((Colorable) e).getColor());
        } else if (AXOLOTL_CLASS != null && AXOLOTL_CLASS.isAssignableFrom(ent.getClass())) {
            return e -> AxolotlVariant.getType(((Axolotl) e).getVariant());
        } else if (ent instanceof Cat) {
            return e -> CatVariant.getType(((Cat) e).getCatType());
        } else if (ent instanceof Fox) {
            return e -> FoxVariant.getType(((Fox) e).getFoxType());
        } else if (ent instanceof Llama) {
            return e -> LlamaVariant.getType(((Llama) e).getColor());
        } else if (ent instanceof MushroomCow) {
            return e -> MooshroomVariant.getType(((MushroomCow) e).getVariant());
        } else if (ent instanceof Parrot) {
            return e -> ParrotVariant.getType(((Parrot) e).getVariant());
        } else if (ent instanceof Rabbit) {
            return e -> RabbitVariant.getType(((Rabbit) e).getRabbitType());
        } else if (ent instanceof Horse) {
            return e -> HorseVariant.getVariant((Horse) e);
        } else if (ent instanceof TropicalFish) {
            return e -> TropicalFishVariant.getVariant((TropicalFish) e);
        } else if (ent instanceof Frog) {
            return e -> FrogVariant.getType(((Frog) e).getVariant());
        } else {
            throw new IllegalArgumentException("Not a type of interest: " + ent.getType());
        }
    }

    public static Function<Entity, ?> getFunctionForType(MobType type) {
        if (type == MobType.sheep || type == MobType.sheep) {
            return e -> DyeVariant.getType(((Colorable) e).getColor());
        } else if (type == MobType.axolotl) {
            return e -> AxolotlVariant.getType(((Axolotl) e).getVariant());
        } else if (type == MobType.cat) {
            return e -> CatVariant.getType(((Cat) e).getCatType());
        } else if (type == MobType.fox) {
            return e -> FoxVariant.getType(((Fox) e).getFoxType());
        } else if (type == MobType.llama) {
            return e -> LlamaVariant.getType(((Llama) e).getColor());
        } else if (type == MobType.mooshroom) {
            return e -> MooshroomVariant.getType(((MushroomCow) e).getVariant());
        } else if (type == MobType.parrot) {
            return e -> ParrotVariant.getType(((Parrot) e).getVariant());
        } else if (type == MobType.rabbit) {
            return e -> RabbitVariant.getType(((Rabbit) e).getRabbitType());
        } else if (type == MobType.horse) {
            return e -> HorseVariant.getVariant((Horse) e);
        } else if (type == MobType.tropical_fish) {
            return e -> TropicalFishVariant.getVariant((TropicalFish) e);
        } else if (type == MobType.frog) {
            return e -> FrogVariant.getType(((Frog) e).getVariant());
        } else {
            throw new IllegalArgumentException("Not a type of interest: " + type);
        }
    }

}
