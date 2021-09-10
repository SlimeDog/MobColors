package dev.ratas.mobcolors.config.variants;

import dev.ratas.mobcolors.utils.Triple;

public class ThreeTypeComplexVariant<E1 extends Enum<E1>, E2 extends Enum<E2>, E3 extends Enum<E3>>
        extends Triple<E1, E2, E3> implements ComplexMobTypeVariant {
    private static final String DEFAULT_DELIMITER = "/";
    private final String delimiter;

    public ThreeTypeComplexVariant(E1 e1, E2 e2, E3 e3) {
        this(e1, e2, e3, DEFAULT_DELIMITER);
    }

    public ThreeTypeComplexVariant(E1 e1, E2 e2, E3 e3, String delimiter) {
        super(e1, e2, e3);
        this.delimiter = delimiter;
    }

    @Override
    public String getName() {
        return getOne() + getDelimiter() + getTwo() + getDelimiter() + getThree();
    }

    @Override
    public String getDelimiter() {
        return delimiter;
    }

}