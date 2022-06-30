package dev.ratas.mobcolors.mock;

import java.io.File;
import java.util.logging.Logger;

import org.junit.jupiter.api.Assertions;

import dev.ratas.mobcolors.region.version.mock.MockScheduler;
import dev.ratas.slimedogcore.api.SlimeDogPlugin;
import dev.ratas.slimedogcore.api.config.SDCCustomConfigManager;
import dev.ratas.slimedogcore.api.config.settings.SDCBaseSettings;
import dev.ratas.slimedogcore.api.messaging.recipient.SDCRecipient;
import dev.ratas.slimedogcore.api.reload.SDCReloadManager;
import dev.ratas.slimedogcore.api.scheduler.SDCScheduler;
import dev.ratas.slimedogcore.api.utils.logger.SDCDebugLogger;
import dev.ratas.slimedogcore.api.wrappers.SDCOnlinePlayerProvider;
import dev.ratas.slimedogcore.api.wrappers.SDCPluginInformation;
import dev.ratas.slimedogcore.api.wrappers.SDCPluginManager;
import dev.ratas.slimedogcore.api.wrappers.SDCResourceProvider;
import dev.ratas.slimedogcore.api.wrappers.SDCWorldProvider;

public class MockSlimeDogPlugin implements SlimeDogPlugin {
    private static final Logger LOGGER = Logger.getLogger("[MockSlimeDog]");
    private final File dataFolder = new File("src" + File.separator + "test" + File.separator + "resources");
    private final MockScheduler scheduler = new MockScheduler();
    private final File testConfig = new File(dataFolder, "config.yml");
    private final File realConfig = new File(
            "src" + File.separator + "main" + File.separator + "resources" + File.separator + "config.yml");
    private final MockConfig config;
    private final MockConfigManager configManager;
    private Logger logger;

    public MockSlimeDogPlugin() {
        this(false);
    }

    public MockSlimeDogPlugin(boolean useRealConfig) {
        if (useRealConfig) {
            config = new MockConfig(realConfig);
        } else {
            config = new MockConfig(testConfig);
        }
        configManager = new MockConfigManager(config);
        this.logger = LOGGER;
    }

    @Override
    public File getDataFolder() {
        return dataFolder;
    }

    @Override
    public File getWorldFolder() {
        return dataFolder;
    }

    @Override
    public SDCScheduler getScheduler() {
        return scheduler;
    }

    @Override
    public SDCPluginManager getPluginManager() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SDCWorldProvider getWorldProvider() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SDCResourceProvider getResourceProvider() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SDCCustomConfigManager getCustomConfigManager() {
        return configManager;
    }

    @Override
    public SDCPluginInformation getPluginInformation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void pluginEnabled() {
        // TODO Auto-generated method stub

    }

    @Override
    public void pluginDisabled() {
        // TODO Auto-generated method stub

    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public SDCDebugLogger getDebugLogger() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SDCBaseSettings getBaseSettings() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SDCRecipient getConsoleRecipient() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SDCOnlinePlayerProvider getOnlinePlayerProvider() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SDCReloadManager getReloadManager() {
        // TODO Auto-generated method stub
        return null;
    }

    public void throwWithLogger() {
        if (logger instanceof ThrowingLogger) {
            return;
        }
        logger = new ThrowingLogger();
    }

    public void stopThrowingWithLogger() {
        if (!(logger instanceof ThrowingLogger)) {
            return;
        }
        logger = LOGGER;
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
