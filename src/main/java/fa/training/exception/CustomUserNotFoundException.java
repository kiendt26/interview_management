package fa.training.exception;

import org.springframework.security.core.AuthenticationException;

public class CustomUserNotFoundException extends AuthenticationException {

    public CustomUserNotFoundException(String msg) {
        super(msg);
    }
}
