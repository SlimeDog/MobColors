package dev.ratas.mobcolors.config.variants;

import java.util.EnumMap;
import java.util.Map;

public interface MobTypeVariant<T extends Enum<T>> {

    T getBukkitVariant();

    public class ReverseTranslator<U extends Enum<U>, V extends MobTypeVariant<?>> {
        private final Map<U, V> map;

        protected ReverseTranslator(Class<U> clazz) {
            map = new EnumMap<>(clazz);
        }

        protected boolean isEmpty() {
            return map.isEmpty();
        }

        @SuppressWarnings("unchecked")
        protected void fill(V... values) {
            for (V val : values) {
                map.put((U) val.getBukkitVariant(), val);
            }
        }

        protected V get(U val) {
            return map.get(val);
        }

    }

}
