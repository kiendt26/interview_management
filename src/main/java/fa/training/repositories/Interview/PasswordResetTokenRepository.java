package fa.training.repositories.Interview;

import fa.training.entities.PasswordResetToken;
import fa.training.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

   PasswordResetToken findByToken(String token);
   PasswordResetToken findByUser(User user);
}
