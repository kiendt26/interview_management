package fa.training.enums;

public enum CurrentPosition {
    BACKEND_DEVELOPER("Backend Developer"),
    BUSINESS_ANALYST("Business Analyst"),
    TESTER("Tester"),
    HR("HR"),
    PROJECT_MANAGER("Project manager"),
    NOT_AVAILABLE("Not available");

    private final String displayName;

    CurrentPosition(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    }
