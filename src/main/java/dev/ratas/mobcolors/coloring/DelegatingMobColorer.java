package dev.ratas.mobcolors.coloring;

import java.util.function.BiConsumer;

import dev.ratas.mobcolors.coloring.settings.ColorMap;
import dev.ratas.mobcolors.config.mob.MobSettings;
import dev.ratas.mobcolors.scheduling.abstraction.Scheduler;

public class DelegatingMobColorer<E, T> extends AbstractMobColorer<E, T> {
    private final Scheduler scheduler;
    private final BiConsumer<E, T> function;

    public DelegatingMobColorer(Scheduler scheduler, MobSettings settings, ColorMap<T> colorMap,
            BiConsumer<E, T> function) {
        super(settings, colorMap);
        this.scheduler = scheduler;
        this.function = function;
    }

    @Override
    public void color(E entity, boolean nextTick) {
        if (nextTick) {
            scheduler.schedule(() -> color(entity, false));
            return;
        }
        try {
            function.accept(entity, rollColor().getColor());
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

}
