package dev.ratas.mobcolors.config.mob;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Stream;

import org.bukkit.configuration.InvalidConfigurationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import dev.ratas.mobcolors.config.Messages;
import dev.ratas.mobcolors.config.Settings;
import dev.ratas.mobcolors.mock.MockSlimeDogPlugin;
import dev.ratas.slimedogcore.api.SlimeDogPlugin;

public class MobTypeTests {
    private static Settings settings;

    @BeforeAll
    public static void setup() {
        SlimeDogPlugin mockPlugin = new MockSlimeDogPlugin(true);
        Messages msgs;
        try {
            msgs = new Messages(mockPlugin);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
        settings = new Settings(mockPlugin, mockPlugin.getCustomConfigManager(), msgs, mockPlugin.getPluginManager(),
                mockPlugin.getScheduler());
    }

    @Test
    public void test_ConfigHasAllMobTypes() {
        Set<MobType> notMapped = EnumSet.allOf(MobType.class);
        for (String mobName : settings.getMobsConfig().getKeys(false)) {
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
        return settings.getMobsConfig().getKeys(false).stream();
    }

}
