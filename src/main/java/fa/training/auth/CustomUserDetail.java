package fa.training.auth;

import fa.training.entities.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetail implements UserDetails {

    final User userDB;

    public CustomUserDetail(User userDB) {
        this.userDB = userDB;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = this.userDB.getRole().name();

        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return userDB.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return userDB.getUserName();
    }


}