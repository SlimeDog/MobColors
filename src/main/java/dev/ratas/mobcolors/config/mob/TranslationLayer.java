package dev.ratas.mobcolors.config.mob;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.DyeColor;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.TropicalFish;

import dev.ratas.mobcolors.config.HorseVariant;
import dev.ratas.mobcolors.config.TropicalFishVariant;

public class TranslationLayer {
    public static final String DEFAULT_SHULKER_TYPE_NAME = "#DEFAULT#";
    private final Map<String, String> usedTranslations = new HashMap<>();
    private final Map<String, String> reverseTranslations = new HashMap<>();

    public String attemptTranslation(final String origName, Class<?> clazz) {
        String translatedName;
        if (Cat.Type.class.isAssignableFrom(clazz)) {
            translatedName = fixCatTypeNames(origName);
        } else if (Fox.Type.class.isAssignableFrom(clazz)) {
            translatedName = fixFoxTypeNames(origName);
        } else if (HorseVariant.class.isAssignableFrom(clazz)) {
            String parts[] = origName.split(HorseVariant.DELIMITER);
            parts[0] = fixHorseColorNames(parts[0]);
            if (parts.length > 1) {
                parts[1] = fixHorseStyleNames(parts[1]);
            }
            translatedName = String.join(HorseVariant.DELIMITER, parts);
        } else if (Llama.Color.class.isAssignableFrom(clazz)) {
            translatedName = fixLlamaColorNames(origName);
        } else if (Rabbit.Type.class.isAssignableFrom(clazz)) {
            translatedName = fixRabbitTypeNames(origName);
        } else if (TropicalFishVariant.class.isAssignableFrom(clazz)) {
            translatedName = fixTropicalFishNames(origName);
        } else if (DyeColor.class.isAssignableFrom(clazz)) {
            // TODO - make sure this is actually a shulker!
            translatedName = fixShulkerNames(origName);
        } else {
            translatedName = origName;
        }
        if (translatedName != origName && !origName.equalsIgnoreCase("random")) { // there was a translation made if
                                                                                  // it's a different instance
                                                                                  // but ignore the tropical fish random
            translatedName = translatedName.toUpperCase().replace("-", "_").replace(" ", "_");
            usedTranslations.put(origName, translatedName);
            reverseTranslations.put(translatedName.toLowerCase(), origName);
        } else {
            translatedName = translatedName.toUpperCase().replace("-", "_").replace(" ", "_");
        }
        return translatedName;
    }

    public String reverseTranslate(String name) {
        String from = reverseTranslations.get(name.toLowerCase());
        return from == null ? name : from;
    }

    private String fixCatTypeNames(String name) {
        switch (name.toLowerCase()) {
            case "britishshorthair":
                return "british_shorthair";
            case "tuxedo":
                return "all_black";
        }
        return name;
    }

    private String fixFoxTypeNames(String name) {
        if (name.equalsIgnoreCase("white")) {
            return "snow";
        }
        return name;
    }

    private String fixHorseColorNames(String name) {
        if (name.equalsIgnoreCase("darkbrown")) {
            return "dark_brown";
        }
        return name;
    }

    private String fixHorseStyleNames(String name) {
        switch (name.toLowerCase()) {
            case "whitedots":
                return "white_dots";
            case "blackdots":
                return "black_dots";
        }
        return name;
    }

    private String fixLlamaColorNames(String name) {
        if (name.equalsIgnoreCase("cream")) {
            return "creamy";
        }
        return name;
    }

    private String fixRabbitTypeNames(String name) {
        switch (name.toLowerCase()) {
            case "killer":
                return "the_killer_bunny";
        }
        return name;
    }

    private String fixTropicalFishNames(String name) {
        if (name.equalsIgnoreCase("random")) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            TropicalFish.Pattern[] patterns = TropicalFish.Pattern.values();
            int nr = random.nextInt(patterns.length);
            TropicalFish.Pattern pattern = patterns[nr];
            DyeColor[] colors = DyeColor.values();
            nr = random.nextInt(colors.length);
            DyeColor color1 = colors[nr];
            nr = random.nextInt(colors.length);
            DyeColor color2 = colors[nr];
            return pattern.name() + TropicalFishVariant.DELIMITER + color1.name() + TropicalFishVariant.DELIMITER
                    + color2.name();
        }
        return name;
    }

    private String fixShulkerNames(String name) {
        if (name.equalsIgnoreCase("default")) {
            return DEFAULT_SHULKER_TYPE_NAME;
        }
        return name;
    }

}
