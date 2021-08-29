package dev.ratas.mobcolors.config.mob;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.EntityType;

public class EntityTypeTranslationLayer {
    private final Map<String, EntityType> usedTranslations = new HashMap<>();
    private final Map<EntityType, String> reverseTranslations = new EnumMap<>(EntityType.class);

    public String attemptTranslation(final String origName) {
        String translatedName;
        EntityType translatedType;
        if (origName.equalsIgnoreCase("mooshroom")) {
            translatedName = EntityType.MUSHROOM_COW.name();
            translatedType = EntityType.MUSHROOM_COW;
        } else {
            translatedName = origName;
            translatedType = null;
        }
        if (translatedName != origName) { // there was a translation made if it's a different instance
            usedTranslations.put(origName, translatedType);
            reverseTranslations.put(translatedType, origName);
        }
        return translatedName;
    }

    public String reverseTranslate(EntityType type) {
        String from = reverseTranslations.get(type);
        return from == null ? type.name().toLowerCase() : from;
    }

}
