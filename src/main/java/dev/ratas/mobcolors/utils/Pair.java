package dev.ratas.mobcolors.utils;

public class Pair<T1, T2> {
    private final T1 t1;
    private final T2 t2;

    public Pair(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public T1 getOne() {
        return t1;
    }

    public T2 getTwo() {
        return t2;
    }

}
