package dev.ratas.mobcolors.config.variants;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.bukkit.entity.Entity;

public class MobVariantProvider<T extends MobTypeVariant<?>, E extends Entity> {
    private final Function<E, T> getter;

    public MobVariantProvider(Function<E, T> getter) {
        this.getter = getter;
    }

    public T getVariant(E e) {
        return getter.apply(e);
    }

    public class InstanceTracker<V, U> {
        private final Map<V, U> map = new HashMap<>();
        private final Function<V, U> constructor;

        protected InstanceTracker(Function<V, U> constructor) {
            this.constructor = constructor;
        }

        protected U get(V v) {
            return map.computeIfAbsent(v, key -> constructor.apply(v));
        }

    }

}
