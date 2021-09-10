package dev.ratas.mobcolors.config.variants;

import dev.ratas.mobcolors.utils.Pair;

public class TwoTypeComplexVariant<E1 extends Enum<E1>, E2 extends Enum<E2>> extends Pair<E1, E2>
        implements ComplexMobTypeVariant {
    private static final String DEFAULT_DELIMITER = "/";
    private final String delimiter;

    public TwoTypeComplexVariant(E1 e1, E2 e2, String delimiter) {
        super(e1, e2);
        this.delimiter = delimiter;
    }

    public TwoTypeComplexVariant(E1 e1, E2 e2) {
        this(e1, e2, DEFAULT_DELIMITER);
    }

    @Override
    public String getName() {
        return getOne() + getDelimiter() + getTwo();
    }

    @Override
    public String getDelimiter() {
        return delimiter;
    }

}