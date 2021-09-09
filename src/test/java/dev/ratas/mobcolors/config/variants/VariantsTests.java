package dev.ratas.mobcolors.config.variants;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import dev.ratas.mobcolors.config.abstraction.SettingsConfigProvider;
import dev.ratas.mobcolors.config.mob.MobType;
import dev.ratas.mobcolors.config.mob.MobTypes;
import dev.ratas.mobcolors.config.mock.FileResourceProvider;
import dev.ratas.mobcolors.config.mock.FileSettingsConfigProvider;

public class VariantsTests {
    private static final Logger LOGGER = Logger.getLogger("[MobColors TEST]");
    private static FileResourceProvider provider;
    private static SettingsConfigProvider configProvider;

    @BeforeAll
    public static void setup() {
        provider = new FileResourceProvider(LOGGER);
        configProvider = new FileSettingsConfigProvider(provider, null);
    }

    // this checks that all he type names within the config are names of the enum
    // however, it does not check that all enum constants exist in the config
    // because they don't
    @ParameterizedTest
    @MethodSource("provideArgumentsForTest")
    public <T extends Enum<T>> void test_VariantsHaveCorrectNames(String mobType, Class<T> clazz) {
        Set<T> leftovers = EnumSet.allOf(clazz);
        ConfigurationSection mobSection = configProvider.getMobsConfig().getConfigurationSection(mobType);
        for (String schemeName : mobSection.getKeys(false)) {
            ConfigurationSection schemeSection = mobSection.getConfigurationSection(schemeName + ".probabilities");
            if (schemeSection == null) {
                continue; // general options such as: enabled, include-leashed, include-pets
            }
            for (String varName : schemeSection.getKeys(false)) {
                if (varName.equals("default")) {
                    varName = "DEFAULT"; // only change
                }
                try {
                    leftovers.remove(Enum.valueOf(clazz, varName)); // thorws exception if no var by name
                } catch (IllegalArgumentException e) {
                    Assertions.assertNull(e,
                            "Could not find enum value " + varName + " for " + mobType + " in " + schemeName);
                }
            }
        }
        if (!leftovers.isEmpty()) {
            System.err.println("NB! Not all " + clazz.getSimpleName() + " types were present in config. "
                    + "Thus some were unable to be checked for name correctness. Missing: " + leftovers);
        }
    }

    private static List<Arguments> provideArgumentsForTest() {
        List<Arguments> list = new ArrayList<>();
        for (Class<? extends MobTypeVariant<?>> clazz : MobTypes.VARIANT_MOB_TYPES.keySet()) {
            for (MobType type : MobTypes.VARIANT_MOB_TYPES.get(clazz)) {
                list.add(Arguments.of(type.name(), clazz));
            }
        }
        return list;
    }

}
