package dev.ratas.mobcolors.config.mob;

import java.util.EnumSet;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import dev.ratas.mobcolors.config.abstraction.SettingsConfigProvider;
import dev.ratas.mobcolors.config.mock.FileResourceProvider;
import dev.ratas.mobcolors.config.mock.FileSettingsConfigProvider;

public class MobTypeTests {
    private static final Logger LOGGER = Logger.getLogger("[MobColors TEST]");
    private static FileResourceProvider provider;
    private static SettingsConfigProvider configProvider;

    @BeforeAll
    public static void setup() {
        provider = new FileResourceProvider(LOGGER);
        configProvider = new FileSettingsConfigProvider(provider, null);
    }

    @Test
    public void test_ConfigHasAllMobTypes() {
        Set<MobType> notMapped = EnumSet.allOf(MobType.class);
        for (String mobName : configProvider.getMobsConfig().getKeys(false)) {
            notMapped.remove(MobType.valueOf(mobName)); // throws exception for undefined mob type
        }
        Assertions.assertTrue(notMapped.isEmpty(), "Not all MobType values were present in config. "
                + "Thus some were unable to be checked for name correctness. Missing: " + notMapped);
    }

    @ParameterizedTest
    @MethodSource("provideDyeVariants")
    public void test_ConfigTypeNamesAreEnums(String mobName) {
        try {
            MobType.valueOf(mobName);
        } catch (IllegalArgumentException e) {
            Assertions.assertNull(e, "Could not find mob type " + mobName);
        }
    }

    private static Stream<String> provideDyeVariants() {
        return configProvider.getMobsConfig().getKeys(false).stream();
    }

}
