package fa.training.enums;

public enum Role {
    ADMIN("Admin"),
    HR("HR Manager"),
    RECRUITER("Recruiter");
    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
