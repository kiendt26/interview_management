package fa.training.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class Handler {
    @ExceptionHandler(EnumMismatchException.class)
    public String enumMismatchHandler(EnumMismatchException e) {
        return e.getMessage();
    }
}
