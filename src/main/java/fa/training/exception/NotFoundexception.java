package fa.training.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotFoundexception extends Exception{

    public NotFoundexception(String message, int status) {
        super(message);
        this.status = status;
        this.message = message;
    }

    int status;
    String message;


}
