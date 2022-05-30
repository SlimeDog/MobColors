package dev.ratas.mobcolors.config.world;

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
import dev.ratas.mobcolors.config.mob.MobSettings;
import dev.ratas.mobcolors.config.variants.AxolotlVariant;
import dev.ratas.mobcolors.config.variants.CatVariant;
import dev.ratas.mobcolors.config.variants.DyeVariant;
import dev.ratas.mobcolors.config.variants.FoxVariant;
import dev.ratas.mobcolors.config.variants.LlamaVariant;
import dev.ratas.mobcolors.config.variants.MooshroomVariant;
import dev.ratas.mobcolors.config.variants.ParrotVariant;
import dev.ratas.mobcolors.config.variants.RabbitVariant;
import dev.ratas.mobcolors.config.variants.TropicalFishVariant;
import dev.ratas.mobcolors.scheduling.abstraction.Scheduler;

public class ColorerGenerator {

    private ColorerGenerator() {
        throw new IllegalStateException("Not allowed");
    }

    // all the colormap casting is unchecked
    @SuppressWarnings("unchecked")
    static MobColorer<?, ?> generateColorer(ColorMap<?> map, MobSettings settings, Scheduler scheduler) {
        switch (map.getApplicableEntityType()) {
            case sheep:
            case shulker:
                return new DelegatingMobColorer<Colorable, DyeVariant>(scheduler, settings, (ColorMap<DyeVariant>) map,
                        (ent, color) -> ent.setColor(color.getBukkitVariant()),
                        ent -> DyeVariant.getType(ent.getColor()));
            case axolotl:
                return new DelegatingMobColorer<Axolotl, AxolotlVariant>(scheduler, settings,
                        (ColorMap<AxolotlVariant>) map, (ax, var) -> ax.setVariant(var.getBukkitVariant()),
                        ax -> AxolotlVariant.getType(ax.getVariant()));
            case cat:
                return new DelegatingMobColorer<Cat, CatVariant>(scheduler, settings, (ColorMap<CatVariant>) map,
                        (cat, type) -> cat.setCatType(type.getBukkitVariant()),
                        cat -> CatVariant.getType(cat.getCatType()));
            case fox:
                return new DelegatingMobColorer<Fox, FoxVariant>(scheduler, settings, (ColorMap<FoxVariant>) map,
                        (fox, type) -> fox.setFoxType(type.getBukkitVariant()),
                        fox -> FoxVariant.getType(fox.getFoxType()));
            case llama:
                return new DelegatingMobColorer<Llama, LlamaVariant>(scheduler, settings, (ColorMap<LlamaVariant>) map,
                        (llama, color) -> llama.setColor(color.getBukkitVariant()),
                        llama -> LlamaVariant.getType(llama.getColor()));
            case mooshroom:
                return new DelegatingMobColorer<MushroomCow, MooshroomVariant>(scheduler, settings,
                        (ColorMap<MooshroomVariant>) map, (cow, var) -> cow.setVariant(var.getBukkitVariant()),
                        cow -> MooshroomVariant.getType(cow.getVariant()));
            case parrot:
                return new DelegatingMobColorer<Parrot, ParrotVariant>(scheduler, settings,
                        (ColorMap<ParrotVariant>) map, (parrot, var) -> parrot.setVariant(var.getBukkitVariant()),
                        parrot -> ParrotVariant.getType(parrot.getVariant()));
            case rabbit:
                return new DelegatingMobColorer<Rabbit, RabbitVariant>(scheduler, settings,
                        (ColorMap<RabbitVariant>) map, (rabbit, type) -> rabbit.setRabbitType(type.getBukkitVariant()),
                        rabbit -> RabbitVariant.getType(rabbit.getRabbitType()));
            case horse:
                return new DelegatingMobColorer<Horse, HorseVariant>(scheduler, settings, (ColorMap<HorseVariant>) map,
                        (horse, var) -> {
                            horse.setColor(var.getOne().getBukkitVariant());
                            horse.setStyle(var.getTwo().getBukkitVariant());
                        }, horse -> HorseVariant.getVariant(horse));
            case tropical_fish:
                return new DelegatingMobColorer<TropicalFish, TropicalFishVariant>(scheduler, settings,
                        (ColorMap<TropicalFishVariant>) map, (fish, var) -> {
                            fish.setPattern(var.getOne().getBukkitVariant());
                            fish.setBodyColor(var.getTwo().getBukkitVariant());
                            fish.setPatternColor(var.getThree().getBukkitVariant());
                        }, fish -> TropicalFishVariant.getVariant(fish));
            default:
                throw new IllegalStateException("No colorer defined for " + map.getApplicableEntityType());
        }
    }

}
