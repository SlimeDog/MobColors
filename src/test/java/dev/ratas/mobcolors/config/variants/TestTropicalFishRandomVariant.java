package dev.ratas.mobcolors.config.variants;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestTropicalFishRandomVariant {

    @Test
    public void test_TropicalFishRandomVariantVaries() {
        String varName = "random";
        TropicalFishVariant var1 = TropicalFishVariant.valueOf(varName);
        TropicalFishVariant var2 = TropicalFishVariant.valueOf(varName);
        if (var2 == var1) { // low chance, but lets roll the dice again
            var2 = TropicalFishVariant.valueOf(varName);
        }
        Assertions.assertNotSame(var1, var2, "Random tropical fish are the same");
        Assertions.assertNotEquals(var1, var2, "Random tropical fish are equal");
    }

}
