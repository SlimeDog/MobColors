package dev.ratas.mobcolors.utils;

import org.bukkit.entity.Entity;

public final class PetUtils {
    private static final String MY_PET_METADATA_KEY = "MyPet";

    private PetUtils() {

    }

    public static boolean isPet(Entity entity) {
        if (entity.hasMetadata(MY_PET_METADATA_KEY)) {
            return true;
        }
        // TODO - other pet plugin
        return false;
    }

}
