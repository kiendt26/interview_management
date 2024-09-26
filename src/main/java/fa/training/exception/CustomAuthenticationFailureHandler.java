package fa.training.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "Invalid username or password"; // Thông báo mặc định

        if (exception.getMessage().contains("User not found")) {
            errorMessage = "User not found";
        } else if (exception.getMessage().contains("User is not active")) {
            errorMessage = "User is not active";
        }

        request.getSession().setAttribute("errorMessage", errorMessage);
        response.sendRedirect("/login?error=true");
    }
}
