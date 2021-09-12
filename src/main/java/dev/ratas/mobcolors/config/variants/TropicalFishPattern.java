package dev.ratas.mobcolors.config.variants;

import org.bukkit.entity.TropicalFish;

public enum TropicalFishPattern implements MobTypeVariant<TropicalFish.Pattern> {
    kob(TropicalFish.Pattern.KOB), sunstreak(TropicalFish.Pattern.SUNSTREAK), snooper(TropicalFish.Pattern.SNOOPER),
    dasher(TropicalFish.Pattern.DASHER), brinely(TropicalFish.Pattern.BRINELY), spotty(TropicalFish.Pattern.SPOTTY),
    flopper(TropicalFish.Pattern.FLOPPER), stripey(TropicalFish.Pattern.STRIPEY), glitter(TropicalFish.Pattern.GLITTER),
    blockfish(TropicalFish.Pattern.BLOCKFISH), betty(TropicalFish.Pattern.BETTY),
    clayfish(TropicalFish.Pattern.CLAYFISH);

    private static final ReverseTranslator<TropicalFish.Pattern, TropicalFishPattern> REVERSE_MAP = new ReverseTranslator<>(
            TropicalFish.Pattern.class);

    private final TropicalFish.Pattern pattern;

    private TropicalFishPattern(TropicalFish.Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public TropicalFish.Pattern getBukkitVariant() {
        return pattern;
    }

    public static TropicalFishPattern getType(TropicalFish.Pattern type) {
        if (REVERSE_MAP.isEmpty()) { // initializing here since it's not possible to access static final objects
                                     // on enum constant initialization
            REVERSE_MAP.fill(values());
        }
        return REVERSE_MAP.get(type);
    }

}
