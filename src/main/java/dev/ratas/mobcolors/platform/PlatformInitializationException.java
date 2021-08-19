package dev.ratas.mobcolors.platform;

public class PlatformInitializationException extends IllegalStateException {

    public PlatformInitializationException(String msg) {
        super("Problem initializing platform: " + msg);
    }
    
}
