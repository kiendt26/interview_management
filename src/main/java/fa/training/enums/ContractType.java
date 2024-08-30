package fa.training.enums;

public enum ContractType {
    TRIAL       ("Trial 2 months"),
    TRAINEE     ("Trainee 3 months"),
    ONE_YEAR    ("1 year"),
    THREE_YEARS ("3 years"),
    UNLIMITED   ("Unlimited");

    private final String message;

    ContractType(String message){
        this.message = message;
    }

    public String getDisplayName(){
        return message;
    }
}
