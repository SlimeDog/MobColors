package dev.ratas.mobcolors.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.ratas.mobcolors.mock.MockSlimeDogPlugin;

public class TestDefaultConfig {
    private MockSlimeDogPlugin mockPlugin;

    @BeforeEach
    public void setup() {
        mockPlugin = new MockSlimeDogPlugin();
        mockPlugin.throwWithLogger();
    }

    @Test
    public void test_DefaultConfigHasCorrectEnumNames() {
        // the assertion is done within the logger - whenever there's a warning, an
        // assertions is made
        Messages msgs;
        try {
            msgs = new Messages(mockPlugin);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
        new Settings(mockPlugin, mockPlugin.getCustomConfigManager(), msgs, mockPlugin.getPluginManager(),
                mockPlugin.getScheduler());
    }

}
