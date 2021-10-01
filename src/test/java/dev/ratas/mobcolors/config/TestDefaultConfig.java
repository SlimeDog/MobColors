package dev.ratas.mobcolors.config;

import java.util.logging.Logger;

import org.bukkit.configuration.InvalidConfigurationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.ratas.mobcolors.PluginProvider;
import dev.ratas.mobcolors.config.abstraction.ResourceProvider;
import dev.ratas.mobcolors.config.abstraction.SettingsConfigProvider;
import dev.ratas.mobcolors.config.mock.DummyResourceProvider;
import dev.ratas.mobcolors.config.mock.FileResourceProvider;
import dev.ratas.mobcolors.config.mock.FileSettingsConfigProvider;
import dev.ratas.mobcolors.config.mock.MockPluginProvider;

public class TestDefaultConfig {
    private static final Logger LOGGER = new ThrowingLogger();
    private SettingsConfigProvider provider;
    private PluginProvider pluginProvider;

    @BeforeEach
    public void setup() {
        provider = new FileSettingsConfigProvider(new FileResourceProvider(LOGGER), LOGGER);
        pluginProvider = new MockPluginProvider();
    }

    @Test
    public void test_DefaultConfigHasCorrectEnumNames() {
        // the assertion is done within the logger - whenever there's a warning, an
        // assertions is made
        Messages msgs;
        try {
            msgs = new FakeMessages(new DummyResourceProvider(LOGGER));
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
        new Settings(provider, msgs, pluginProvider, null, true); // force enable all mob settings
    }

    private static class ThrowingLogger extends Logger {

        protected ThrowingLogger() {
            super("[MobColors TEST]", null);
        }

        @Override
        public void warning(String message) {
            Assertions.assertTrue(false, "Warning message called with: " + message);
        }

    }

    private class FakeMessages extends Messages {

        public FakeMessages(ResourceProvider resourceProvider) throws InvalidConfigurationException {
            super(resourceProvider);
        }

    }

}
