package fa.training.exception;

public class EnumMismatchException extends IllegalArgumentException{
    public EnumMismatchException(String message) {
        super(message);
    }
}
