package dev.ratas.mobcolors.config.variants;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import dev.ratas.mobcolors.config.ColorSchemeParser;

public class TestShulkerDefaultColor {

    @Test
    public void test_ShulkerDefaultColorMapsCorrectly() {
        String colorName = "default";
        DyeVariant var = ColorSchemeParser.getValueOf(DyeVariant.class, colorName);
        Assertions.assertSame(var, DyeVariant.DEFAULT, "The shulker default color was mapped incorrectly");
    }

}
