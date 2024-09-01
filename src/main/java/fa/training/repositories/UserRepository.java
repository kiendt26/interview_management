package fa.training.repositories;

import fa.training.entities.User;
import fa.training.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String username);
    List<User> findByRoleOrRole(Role role1, Role role2);
}
