package dev.ratas.mobcolors.config.variants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import dev.ratas.mobcolors.config.mob.MobTypes;

public class TestInternalEnumsMapAllBukkitEnums {

    @ParameterizedTest
    @MethodSource("provideArgumentsForTest")
    public void test_InternalEnumMapsAllBukkitEnumValues(Class<Enum<? extends MobTypeVariant<?>>> clazz) {
        Set<Object> usedBukkitVariants = new HashSet<>();
        Class<?> bukkitEnumClass = null;
        for (Object internal : clazz.getEnumConstants()) {
            Object bukkitVariant = ((MobTypeVariant<?>) internal).getBukkitVariant();
            Assertions.assertTrue(usedBukkitVariants.add(bukkitVariant), "Multiple mappings for " + bukkitVariant);
            if (internal != DyeVariant.DEFAULT) { // hacky solution
                Assertions.assertNotNull(bukkitVariant, "No bukkit mapping for " + internal);
                bukkitEnumClass = bukkitVariant.getClass();
            }
        }
        Assertions.assertFalse(usedBukkitVariants.isEmpty(), "No mappings for " + clazz.getSimpleName());
        for (Object bukkit : bukkitEnumClass.getEnumConstants()) {
            Assertions.assertTrue(usedBukkitVariants.contains(bukkit), "No internal enum for " + bukkit);
        }
    }

    private static List<Class<? extends MobTypeVariant<?>>> provideArgumentsForTest() {
        return new ArrayList<>(MobTypes.VARIANT_MOB_TYPES.keySet());

    }

}
