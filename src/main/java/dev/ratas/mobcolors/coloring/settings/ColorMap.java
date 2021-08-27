package dev.ratas.mobcolors.coloring.settings;

import java.util.Collection;
import java.util.Map;

import org.bukkit.entity.EntityType;

/**
 * Represents a color map. A distribution of colors/types/variants. While the
 * type parameter is generally expected to be an Enum, any class with a static
 * valueOf method will do. This is used for Horse and TropicalFish where there's
 * more than one parameter that can be changed.
 */
public interface ColorMap<T> {

    String getName();

    double getChanceFor(T color);

    Map<T, Double> getChoices();

    ColorChoice<T> rollColor();

    EntityType getApplicableEntityType();

    Collection<String> getApplicableWorlds();

}
