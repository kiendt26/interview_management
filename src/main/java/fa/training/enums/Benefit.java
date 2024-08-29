package fa.training.enums;

public enum Benefit {
    LUNCH("Lunch"),
    DAY_LEAVE("25-day leave"),
    HEALTHCARE_INSURANCE("Healthcare insurance"),
    HYBRID_WORKING("Hybrid working"),
    TRAVEL("Travel");

    private final String displayName;

    Benefit(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
