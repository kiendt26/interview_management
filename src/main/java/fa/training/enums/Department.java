package fa.training.enums;


public enum Department {
    IT("IT"),
    HR("HR"),
    FINANCE("Finance"),
    COMMUNICATION("Communication"),
    MARKETING("Marketing"),
    ACCOUNTING("Accounting");

    private final String displayName;

    Department(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
