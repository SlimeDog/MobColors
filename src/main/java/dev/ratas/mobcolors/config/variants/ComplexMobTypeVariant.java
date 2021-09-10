package dev.ratas.mobcolors.config.variants;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import dev.ratas.mobcolors.utils.TriFunction;

public interface ComplexMobTypeVariant {

    String getName();

    String getDelimiter();

    public static class InstanceTracker<T, V> {
        private final Map<T, V> variantsMap = new HashMap<>();
        private final Function<T, V> constructor;

        public InstanceTracker(Function<T, V> constructor) {
            this.constructor = constructor;
        }

        public V get(T variant) {
            return variantsMap.computeIfAbsent(variant, v -> constructor.apply(v));
        }

        public static <T1, T2, V> InstanceTracker<T1, InstanceTracker<T2, V>> getBiTracker(
                BiFunction<T1, T2, V> constructor) {
            return new InstanceTracker<>(t1 -> new InstanceTracker<T2, V>(t2 -> constructor.apply(t1, t2)));
        }

        public static <T1, T2, T3, V> InstanceTracker<T1, InstanceTracker<T2, InstanceTracker<T3, V>>> getTripleTracker(
                TriFunction<T1, T2, T3, V> constructor) {
            return new InstanceTracker<>(t1 -> getBiTracker((t2, t3) -> constructor.apply(t1, t2, t3)));
        }

    }

}
