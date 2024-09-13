package fa.training.enums;

public enum ResultInterview {
    NA("N/A"),
    FAILDED("Failded"),
    PASS("Pass"),
    OPEN("Open");
    private final String displayName;

    ResultInterview(String displayName) {
        this.displayName = displayName;
    }


    public String getDisplayName() {
        return displayName;
    }
}
