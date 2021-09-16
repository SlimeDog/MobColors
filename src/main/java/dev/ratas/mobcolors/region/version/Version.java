package dev.ratas.mobcolors.region.version;

public final class Version {
    private static final String ENTITY_LOAD_EVENT_CLASS_PATH = "org.bukkit.event.world.EntitiesLoadEvent";
    private static final boolean HAS_ENTITIES_LOAD_EVENT;
    static {
        boolean val;
        try {
            Class.forName(ENTITY_LOAD_EVENT_CLASS_PATH);
            val = true;
        } catch (ClassNotFoundException e) {
            val = false;
        }
        HAS_ENTITIES_LOAD_EVENT = val;
    }

    private Version() {

    }

    public static boolean hasEntitiesLoadEvent() {
        return HAS_ENTITIES_LOAD_EVENT;
    }

}
