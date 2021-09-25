package dev.ratas.mobcolors.utils;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * For easy access to the plugin's logger. This does not curretnly work at test
 * time.
 */
public final class LogUtils {
    private static final Logger LOGGER = initializeLogger();

    private LogUtils() {
        throw new IllegalStateException("Should not be initialzied");
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    private static final Logger initializeLogger() {
        // TODO - make work during test time
        return JavaPlugin.getProvidingPlugin(LogUtils.class).getLogger();
    }

}
