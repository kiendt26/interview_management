package fa.training.enums;

public enum Status {

    OPEN("Open"),
    WAITING_FOR_INTERVIEW("Waiting For Interview"),
    CANCELLED_INTERVIEW("Cancelled Interview"),
    PASSED_INTERVIEW("Passed Interview"),
    FAILED_INTERVIEW("Failed Interview"),
    WAITING_FOR_APPROVAL("Waiting For Approval"),
    APPROVED_OFFER("Approved Offer"),
    REJECTED_OFFER("Rejected Offer"),
    WAITING_FOR_RESPONSE("Waiting For Response"),
    ACCEPTED_OFFER("Accepted Offer"),
    DECLINED_OFFER("Declined Offer"),
    CANCELLED_OFFER("Cancelled Offer"),
    BANNED("Banned");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
