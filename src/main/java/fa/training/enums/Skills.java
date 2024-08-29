package fa.training.enums;

public enum Skills {
    JAVA("Java"),
    NODEJS("Nodejs"),
    NET(".net"),
    C("C++"),
    BUSINESS_ANALYSIS("Business analysis"),
    COMMUNICATION("Communication");

    private final String displayName;

    Skills(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
