package dev.ratas.mobcolors.config.variants;

public class TwoTypeComplexVariant<E1 extends Enum<E1>, E2 extends Enum<E2>> implements ComplexMobTypeVariant {
    private static final String DEFAULT_DELIMITER = "/";
    private final E1 e1;
    private final E2 e2;
    private final String delimiter;

    public TwoTypeComplexVariant(E1 e1, E2 e2, String delimiter) {
        this.e1 = e1;
        this.e2 = e2;
        this.delimiter = delimiter;
    }

    public TwoTypeComplexVariant(E1 e1, E2 e2) {
        this(e1, e2, DEFAULT_DELIMITER);
    }

    @Override
    public String getName() {
        return e1 + getDelimiter() + e2;
    }

    @Override
    public String getDelimiter() {
        return delimiter;
    }

}