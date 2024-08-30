package fa.training.enums;

public enum OfferStatus {
    WAITING ("Waiting for Approval"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    RESPONSE("Waiting for Response"),
    ACCEPTED("Accepted Offer"),
    DECLINED("Declined Offer"),
    CANCELED("Cancelled");

    private final String displayName;

    OfferStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
