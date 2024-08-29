package fa.training.enums;

public enum HighestLevel {
    HIGH_SCHOOL("High School"),
    BACHELORS_DEGREE("Bachelor's Degree"),
    MASTER_DEGREE("Master Degree"),
    PHD("PhD");

    private final String displayName;

    HighestLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
