package dev.ratas.mobcolors.coloring;

import java.util.function.BiConsumer;
import java.util.function.Function;

import dev.ratas.mobcolors.coloring.settings.ColorMap;
import dev.ratas.mobcolors.config.mob.MobSettings;
import dev.ratas.slimedogcore.api.scheduler.SDCScheduler;

public class DelegatingMobColorer<E, T> extends AbstractMobColorer<E, T> {
    private final SDCScheduler scheduler;
    private final BiConsumer<E, T> setter;
    private final Function<E, T> getter;

    public DelegatingMobColorer(SDCScheduler scheduler, MobSettings settings, ColorMap<T> colorMap,
            BiConsumer<E, T> setter, Function<E, T> getter) {
        super(settings, colorMap);
        this.scheduler = scheduler;
        this.setter = setter;
        this.getter = getter;
    }

    @Override
    public boolean color(E entity, boolean nextTick) {
        T color = rollColor().getColor();
        boolean isSame = getter.apply(entity) == color;
        if (isSame) {
            return false;
        }
        if (nextTick) {
            scheduler.runTask(() -> colorInternal(entity, color));
        } else {
            colorInternal(entity, color);
        }
        return true;
    }

    private void colorInternal(E entity, T color) {
        try {
            setter.accept(entity, color);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

}
