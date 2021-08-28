package dev.ratas.mobcolors.config.world;

import org.bukkit.DyeColor;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.TropicalFish;
import org.bukkit.material.Colorable;

import dev.ratas.mobcolors.coloring.DelegatingMobColorer;
import dev.ratas.mobcolors.coloring.MobColorer;
import dev.ratas.mobcolors.coloring.settings.ColorMap;
import dev.ratas.mobcolors.config.HorseVariant;
import dev.ratas.mobcolors.config.TropicalFishVariant;
import dev.ratas.mobcolors.config.mob.MobSettings;
import dev.ratas.mobcolors.scheduling.abstraction.Scheduler;

public class ColorerGenerator {

    private ColorerGenerator() {
        throw new IllegalStateException("Not allowed");
    }

    // all the colormap casting is unchecked
    @SuppressWarnings("unchecked")
    static MobColorer<?, ?> generateColorer(ColorMap<?> map, MobSettings settings, Scheduler scheduler) {
        switch (map.getApplicableEntityType()) {
            case SHEEP:
            case SHULKER:
                return new DelegatingMobColorer<Colorable, DyeColor>(scheduler, settings, (ColorMap<DyeColor>) map,
                        (ent, color) -> ent.setColor(color), ent -> ent.getColor());
            case AXOLOTL:
                return new DelegatingMobColorer<Axolotl, Axolotl.Variant>(scheduler, settings,
                        (ColorMap<Axolotl.Variant>) map, (ax, var) -> ax.setVariant(var), ax -> ax.getVariant());
            case CAT:
                return new DelegatingMobColorer<Cat, Cat.Type>(scheduler, settings, (ColorMap<Cat.Type>) map,
                        (cat, type) -> cat.setCatType(type), cat -> cat.getCatType());
            case FOX:
                return new DelegatingMobColorer<Fox, Fox.Type>(scheduler, settings, (ColorMap<Fox.Type>) map,
                        (fox, type) -> fox.setFoxType(type), fox -> fox.getFoxType());
            case LLAMA:
                return new DelegatingMobColorer<Llama, Llama.Color>(scheduler, settings, (ColorMap<Llama.Color>) map,
                        (llama, color) -> llama.setColor(color), llama -> llama.getColor());
            case MUSHROOM_COW:
                return new DelegatingMobColorer<MushroomCow, MushroomCow.Variant>(scheduler, settings,
                        (ColorMap<MushroomCow.Variant>) map, (cow, var) -> cow.setVariant(var),
                        cow -> cow.getVariant());
            case PARROT:
                return new DelegatingMobColorer<Parrot, Parrot.Variant>(scheduler, settings,
                        (ColorMap<Parrot.Variant>) map, (parrot, var) -> parrot.setVariant(var),
                        parrot -> parrot.getVariant());
            case RABBIT:
                return new DelegatingMobColorer<Rabbit, Rabbit.Type>(scheduler, settings, (ColorMap<Rabbit.Type>) map,
                        (rabbit, type) -> rabbit.setRabbitType(type), rabbit -> rabbit.getRabbitType());
            case HORSE:
                return new DelegatingMobColorer<Horse, HorseVariant>(scheduler, settings, (ColorMap<HorseVariant>) map,
                        (horse, var) -> {
                            horse.setColor(var.getOne());
                            horse.setStyle(var.getTwo());
                        }, horse -> HorseVariant.getVariant(horse));
            case TROPICAL_FISH:
                return new DelegatingMobColorer<TropicalFish, TropicalFishVariant>(scheduler, settings,
                        (ColorMap<TropicalFishVariant>) map, (fish, var) -> {
                            fish.setPattern(var.getOne());
                            fish.setBodyColor(var.getTwo());
                            fish.setPatternColor(var.getThree());
                        }, fish -> TropicalFishVariant.getVariant(fish));
            default:
                throw new IllegalStateException("No colorer defined for " + map.getApplicableEntityType());
        }
    }

}
