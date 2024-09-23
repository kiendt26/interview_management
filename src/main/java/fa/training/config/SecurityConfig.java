package fa.training.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class SecurityConfig {

    private static final String[] PERMIT_ALL_LINK = {
            "/login",
            "/forgot-password",
            "/register",
            "/",
            "/css/**",
            "/js/**",
            "/send"
    };
    private static final String[] ADMIN_PERMIT_LINK = {
            "/interview/edit"
    };
    private static final String[] INTERVIEW_PERMIT_LINK = {
            "/interview/submit"
    };
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(Customizer.withDefaults())
                .authorizeRequests(authorize -> authorize
                        .requestMatchers(PERMIT_ALL_LINK).permitAll()
                        .requestMatchers(ADMIN_PERMIT_LINK).hasAnyAuthority("Admin","Recruiter", "Manager")
                        .requestMatchers(INTERVIEW_PERMIT_LINK).hasAuthority("Interview")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(
                        loginForm -> loginForm.loginPage("/login")
                                .loginProcessingUrl("/login-check")
                                .usernameParameter("userName")
                                .passwordParameter("passwordHash")
                                .failureUrl("/login?error=true")
                                .successHandler(new SavedRequestAwareAuthenticationSuccessHandler())
                                .defaultSuccessUrl("/")

                )
                .logout(
                        logout -> logout.logoutUrl("/logout")
                                .logoutSuccessUrl("/login")
                                .invalidateHttpSession(true)
                )
                .csrf(csrf -> csrf.disable());  // Optional: Disable CSRF protection if not needed
        return http.build();

    }


    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    public void configGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

    }
}
