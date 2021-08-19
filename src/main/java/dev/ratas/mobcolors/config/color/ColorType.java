package dev.ratas.mobcolors.config.color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Cat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Rabbit;

public enum ColorType {
    SHEEP_DYE_COLOR(EntityType.SHEEP, DyeColor.class), SHULKER_DYE_COLOR(EntityType.SHULKER, DyeColor.class),
    HORSE_COLOR(EntityType.HORSE, Horse.Color.class), LLAMA_COLOR(EntityType.LLAMA, Llama.Color.class),
    AXOLOTL_VARIANT("AXOLOTL", "org.bukkit.entity.Axolotl.Variant"), CAT_TYPE(EntityType.CAT, Cat.Type.class),
    FOX_TYPE(EntityType.FOX, Fox.Type.class), MUSHROOM_COW_VARIANT(EntityType.MUSHROOM_COW, MushroomCow.Variant.class),
    RABBIT_TYPE(EntityType.RABBIT, Rabbit.Type.class);

    private static final List<String> AVAILABLE_NAMES;
    static {
        List<String> names = new ArrayList<>();
        for (ColorType type : values()) {
            if (type.isValid()) {
                names.add(type.name());
            }
        }
        AVAILABLE_NAMES = Collections.unmodifiableList(names);
    }

    private final EntityType applicableType;
    private final Class<? extends Enum<?>> clazz;

    private ColorType(EntityType applicableType, Class<? extends Enum<?>> clazz) {
        this.applicableType = applicableType;
        this.clazz = clazz;
    }

    // for enum class which may not exist on older MC versions
    @SuppressWarnings("unchecked") // actually checked
    private ColorType(String applicableTypeName, String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.err
                    .println("[MobColors] " + "Could not find color class for color type " + name() + ": " + className);
            System.out.println("This is likely because this type of mob is not available on your version ("
                    + Bukkit.getVersion() + ")");
            clazz = null;
        }
        if (clazz != null) {
            if (!Enum.class.isAssignableFrom(clazz)) {
                System.err.println("[MobColors] " + "The color class found for color type " + name()
                        + " is not a propert enum: " + clazz);
                this.clazz = null;
            } else {
                this.clazz = (Class<? extends Enum<?>>) clazz; // unchecked warning is actually checked
            }
            this.applicableType = EntityType.valueOf(applicableTypeName);
        } else {
            this.clazz = null;
            this.applicableType = null;
        }
    }

    public boolean isValid() {
        return clazz != null;
    }

    public Class<? extends Enum<?>> getColorClass() {
        if (!isValid()) {
            throw new IllegalStateException(
                    "This color type is not available right now (it may not be available on your version, "
                            + "check startup log for details): " + name());
        }
        return clazz;
    }

    public EntityType getApplicableType() {
        return applicableType;
    }

    public static List<String> getNames() {
        return AVAILABLE_NAMES;
    }

}
