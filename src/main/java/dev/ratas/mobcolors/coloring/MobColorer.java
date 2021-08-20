package dev.ratas.mobcolors.coloring;

import dev.ratas.mobcolors.coloring.settings.ColorMap;
import dev.ratas.mobcolors.coloring.settings.ColorDecision;
import dev.ratas.mobcolors.coloring.settings.ColorReason;

public interface MobColorer<E, T> {

    ColorMap<T> getColorMap();

    ColorDecision shouldColor(E entity, ColorReason reason);

    /**
     * Performs the coloring action. Does so on the next tick if needed.
     *
     * @param entity
     * @param nextTick
     */
    void color(E entity, boolean nextTick);

}
