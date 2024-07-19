package dev.ratas.mobcolors.config.variants;

import java.util.HashMap;
import java.util.Map;

public interface MobTypeVariant<T> {

    T getBukkitVariant();

    public class ReverseTranslator<U, V extends MobTypeVariant<?>> {
        private final Map<U, V> map;

        protected ReverseTranslator(Class<U> clazz) {
            map = new HashMap<>();
        }

        protected boolean isEmpty() {
            return map.isEmpty();
        }

        @SuppressWarnings("unchecked")
        protected void fill(V... values) {
            for (V val : values) {
                U bv = (U) val.getBukkitVariant();
                if (bv == null) {
                    continue; // unsupported mob tyeps
                }
                map.put(bv, val);
            }
        }

        protected V get(U val) {
            return map.get(val);
        }

    }

}
