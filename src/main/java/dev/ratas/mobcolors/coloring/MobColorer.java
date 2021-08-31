package dev.ratas.mobcolors.coloring;

import dev.ratas.mobcolors.coloring.settings.ColorMap;
import dev.ratas.mobcolors.coloring.settings.ColorDecision;
import dev.ratas.mobcolors.coloring.settings.ColorReason;
import dev.ratas.mobcolors.region.RegionOptions;

public interface MobColorer<E, T> {

    ColorMap<T> getColorMap();

    ColorDecision shouldColor(E entity, RegionOptions options, ColorReason reason);

    /**
     * Performs the coloring action. Does so on the next tick if needed.
     *
     * @param entity
     * @param nextTick
     */
    boolean color(E entity, boolean nextTick);

}
