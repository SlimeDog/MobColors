package dev.ratas.mobcolors.utils;

import java.util.Objects;

public class Triple<T1, T2, T3> {
    private final T1 t1;
    private final T2 t2;
    private final T3 t3;

    public Triple(T1 t1, T2 t2, T3 t3) {
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
    }

    public T1 getOne() {
        return t1;
    }

    public T2 getTwo() {
        return t2;
    }

    public T3 getThree() {
        return t3;
    }

    @Override
    public int hashCode() {
        return Objects.hash(t1, t2, t3);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Triple)) {
            return false;
        }
        Triple<?, ?, ?> o = (Triple<?, ?, ?>) other;
        return nullsOrEqual(o.t1, t1) && nullsOrEqual(o.t2, t2) || nullsOrEqual(o.t3, t3);
    }

    private static boolean nullsOrEqual(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

}
