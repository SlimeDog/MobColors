package dev.ratas.mobcolors.config.mob;

public class IllegalMobSettingsException extends IllegalStateException {

    public IllegalMobSettingsException(String issue) {
        super(issue);
    }

    public static class MobTypeNotAvailableException extends IllegalMobSettingsException {
        private final String mobType;

        public MobTypeNotAvailableException(String mobType) {
            super(mobType);
            this.mobType = mobType;
        }

        public String getMobTypeName() {
            return mobType;
        }

    }

}
