package dev.ratas.mobcolors.config.variants;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestComplexMobTypeVariantInstanceTracker2 {

    @Test
    public void test_InstanceTrackerMappingMapsSameToSameInstance() {
        DayWeatherType dwt1 = DayWeatherType.getInstance(Day.MONDAY, Weather.RAINY);
        DayWeatherType dwt2 = DayWeatherType.getInstance(Day.MONDAY, Weather.RAINY);
        Assertions.assertSame(dwt1, dwt2, "Instances are different");
        Assertions.assertEquals(dwt1, dwt2, "Instances are not equal");
    }

    @Test
    public void test_InstanceTrackerMappingMapsDifferentToDifferentInstance1() {
        DayWeatherType dwt1 = DayWeatherType.getInstance(Day.MONDAY, Weather.SUNNY);
        DayWeatherType dwt2 = DayWeatherType.getInstance(Day.MONDAY, Weather.RAINY);
        Assertions.assertNotSame(dwt1, dwt2, "Instances are same");
        Assertions.assertNotEquals(dwt1, dwt2, "Instances are equal");
    }

    @Test
    public void test_InstanceTrackerMappingMapsDifferentToDifferentInstance2() {
        DayWeatherType dwt1 = DayWeatherType.getInstance(Day.MONDAY, Weather.SUNNY);
        DayWeatherType dwt2 = DayWeatherType.getInstance(Day.TUESDAY, Weather.RAINY);
        Assertions.assertNotSame(dwt1, dwt2, "Instances are same");
        Assertions.assertNotEquals(dwt1, dwt2, "Instances are equal");
    }

    private static class DayWeatherType extends TwoTypeComplexVariant<Day, Weather> {
        private static final InstanceTracker<Day, InstanceTracker<Weather, DayWeatherType>> INSTANCE_TRACKER = InstanceTracker
                .getBiTracker((day, weather) -> new DayWeatherType(day, weather));

        private DayWeatherType(Day day, Weather weather) {
            super(day, weather);
        }

        private static DayWeatherType getInstance(Day day, Weather weather) {
            return INSTANCE_TRACKER.get(day).get(weather);
        }
    }

    private static enum Day {
        MONDAY, TUESDAY
    }

    private static enum Weather {
        SUNNY, RAINY
    }

}
