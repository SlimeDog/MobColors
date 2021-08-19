package dev.ratas.mobcolors.reload;

import java.util.ArrayList;
import java.util.List;

import dev.ratas.mobcolors.platform.PluginPlatform;

public class ReloadManager {
    private final List<Reloadable> reloadables = new ArrayList<>();

    public void register(Reloadable reloadable) {
        reloadables.add(reloadable);
    }

    public boolean reload(PluginPlatform platform) {
        for (Reloadable reloadable : reloadables) {
            try {
                reloadable.reload();
            } catch (ReloadException e) {
                platform.disableWith(e);
                return false;
            }
        }
        return true;
    }
    
}
