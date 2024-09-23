package fa.training.repositories;

import fa.training.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Objects;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    @Query("SELECT u.email FROM User u WHERE u.userName IN :userNames")
    List<String> findEmailByUserNameIn(@Param("userNames") List<String> userNames);

    User findByUserName(String userName);


    User findByEmail(String email);
}
