package dev.ratas.mobcolors.reload;

public class ReloadException extends IllegalStateException {

    public ReloadException(Reloadable component, String message) {
        super("Problem reloading component " + component + ": " + message);
    }
    
}
