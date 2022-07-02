package dev.ratas.mobcolors.utils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import dev.ratas.mobcolors.config.mob.MobType;

public final class CommandUtils {

    private CommandUtils() {
        throw new IllegalStateException("Should not initialize utiltiy class");
    }

    public static Set<String> getOptions(String[] args) {
        return Arrays.stream(args).filter((arg) -> arg.startsWith("--")).collect(Collectors.toSet());
    }

    public static MobType identifyType(String mobName) {
        MobType type;
        try {
            type = MobType.valueOf(mobName.toLowerCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
        return type.isValid() ? type : null;
    }

    public static MobType getTargetType(String[] args) {
        boolean optionFound = false;
        for (String arg : args) {
            if (optionFound) {
                return identifyType(arg);
            }
            if (arg.equalsIgnoreCase("--mob")) {
                optionFound = true;
            }
        }
        return null; // not enough arguments
    }

}
