package dev.ratas.mobcolors.coloring.settings;

import java.util.Collection;
import java.util.Map;

import org.bukkit.entity.EntityType;

public interface ColorMap<T extends Enum<?>> {

    String getName();

    double getChanceFor(T color);

    Map<T, Double> getChoices();

    ColorChoice<T> rollColor();

    EntityType getApplicableEntityType();

    Collection<String> getApplicableWorlds();

}
