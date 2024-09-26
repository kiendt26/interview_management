package fa.training.auth;

import fa.training.entities.User;
import fa.training.enums.StatusUser;
import fa.training.exception.CustomUserNotFoundException;
import fa.training.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    UsersRepository usersRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = Optional.ofNullable(usersRepository.findByUserName(username));

        if (user.isEmpty()) {
            throw new CustomUserNotFoundException("User not found");
        }

        if (user.get().getStatus() == null || !user.get().getStatus().equals(StatusUser.Active) ) {
            throw new CustomUserNotFoundException("User is not active");
        }
        User userDB = user.get();

        return new CustomUserDetail(userDB);
    }
}