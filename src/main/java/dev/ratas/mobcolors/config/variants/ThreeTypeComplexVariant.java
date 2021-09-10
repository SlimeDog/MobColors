package dev.ratas.mobcolors.config.variants;

public class ThreeTypeComplexVariant<E1 extends Enum<E1>, E2 extends Enum<E2>, E3 extends Enum<E3>>
        implements ComplexMobTypeVariant {
    private static final String DEFAULT_DELIMITER = "/";
    private final E1 e1;
    private final E2 e2;
    private final E3 e3;
    private final String delimiter;

    public ThreeTypeComplexVariant(E1 e1, E2 e2, E3 e3) {
        this(e1, e2, e3, DEFAULT_DELIMITER);
    }

    public ThreeTypeComplexVariant(E1 e1, E2 e2, E3 e3, String delimiter) {
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
        this.delimiter = delimiter;
    }

    @Override
    public String getName() {
        return e1 + getDelimiter() + e2 + getDelimiter() + e3;
    }

    @Override
    public String getDelimiter() {
        return delimiter;
    }

}