package dev.ratas.mobcolors.config.variants;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestComplexMobTypeVariantInstanceTracker3 {

    @Test
    public void test_InstanceTrackerMappingMapsSameToSameInstance() {
        DayWeatherType dwt1 = DayWeatherType.getInstance(Day.MONDAY, Weather.RAINY, Temp.HOT);
        DayWeatherType dwt2 = DayWeatherType.getInstance(Day.MONDAY, Weather.RAINY, Temp.HOT);
        Assertions.assertSame(dwt1, dwt2, "Instances are different");
        Assertions.assertEquals(dwt1, dwt2, "Instances are not equal");
    }

    @Test
    public void test_InstanceTrackerMappingMapsDifferentToDifferentInstance1() {
        DayWeatherType dwt1 = DayWeatherType.getInstance(Day.MONDAY, Weather.SUNNY, Temp.HOT);
        DayWeatherType dwt2 = DayWeatherType.getInstance(Day.MONDAY, Weather.RAINY, Temp.HOT);
        Assertions.assertNotSame(dwt1, dwt2, "Instances are same");
        Assertions.assertNotEquals(dwt1, dwt2, "Instances are equal");
    }

    @Test
    public void test_InstanceTrackerMappingMapsDifferentToDifferentInstance2() {
        DayWeatherType dwt1 = DayWeatherType.getInstance(Day.MONDAY, Weather.SUNNY, Temp.HOT);
        DayWeatherType dwt2 = DayWeatherType.getInstance(Day.TUESDAY, Weather.RAINY, Temp.HOT);
        Assertions.assertNotSame(dwt1, dwt2, "Instances are same");
        Assertions.assertNotEquals(dwt1, dwt2, "Instances are equal");
    }

    @Test
    public void test_InstanceTrackerMappingMapsDifferentToDifferentInstance3() {
        DayWeatherType dwt1 = DayWeatherType.getInstance(Day.MONDAY, Weather.SUNNY, Temp.HOT);
        DayWeatherType dwt2 = DayWeatherType.getInstance(Day.TUESDAY, Weather.RAINY, Temp.COLD);
        Assertions.assertNotSame(dwt1, dwt2, "Instances are same");
        Assertions.assertNotEquals(dwt1, dwt2, "Instances are equal");
    }

    private static class DayWeatherType extends ThreeTypeComplexVariant<Day, Weather, Temp> {
        private static final InstanceTracker<Day, InstanceTracker<Weather, InstanceTracker<Temp, DayWeatherType>>> INSTANCE_TRACKER = InstanceTracker
                .getTripleTracker((day, weather, temp) -> new DayWeatherType(day, weather, temp));

        private DayWeatherType(Day day, Weather weather, Temp temp) {
            super(day, weather, temp);
        }

        private static DayWeatherType getInstance(Day day, Weather weather, Temp temp) {
            return INSTANCE_TRACKER.get(day).get(weather).get(temp);
        }
    }

    private static enum Day {
        MONDAY, TUESDAY
    }

    private static enum Weather {
        SUNNY, RAINY
    }

    private static enum Temp {
        HOT, COLD
    }

}
