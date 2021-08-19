package dev.ratas.mobcolors.utils;

import org.bukkit.ChatColor;

public class StringIgnoreCase {
    private final String string;

    public StringIgnoreCase(String string) {
        this.string = ChatColor.stripColor(string);
    }

    public String getString() {
        return string;
    }

    @Override
    public int hashCode() {
        return string.toLowerCase().hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof StringIgnoreCase)) return false;
        StringIgnoreCase o = (StringIgnoreCase) other;
        return o.string.equalsIgnoreCase(string);
    }

    @Override
    public String toString() {
        return String.format("(ignorecase: %s)", string);
    }

}
