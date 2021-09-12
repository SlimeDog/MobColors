package dev.ratas.mobcolors.config;

import java.util.logging.Logger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.ratas.mobcolors.config.abstraction.SettingsConfigProvider;
import dev.ratas.mobcolors.config.mock.FileResourceProvider;
import dev.ratas.mobcolors.config.mock.FileSettingsConfigProvider;

public class TestDefaultConfig {
    private static final Logger LOGGER = new ThrowingLogger();
    private SettingsConfigProvider provider;

    @BeforeEach
    public void setup() {
        provider = new FileSettingsConfigProvider(new FileResourceProvider(LOGGER), LOGGER);
    }

    @Test
    public void test_DefaultConfigHasCorrectEnumNames() {
        // the assertion is done within the logger - whenever there's a warning, an
        // assertions is made
        new Settings(provider, null);
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

}
