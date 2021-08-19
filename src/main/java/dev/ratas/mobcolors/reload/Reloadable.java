package dev.ratas.mobcolors.reload;

public interface Reloadable {

    /**
     * Reloads component.
     *
     * @throws ReloadException when there's an issue reloading the component
     */
    void reload() throws ReloadException;

}
