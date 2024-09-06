package fa.training.repositories;

<<<<<<< HEAD
import fa.training.entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}
=======
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}
>>>>>>> 7c95720c4d2cd8d07da3815152ffbcccf2b0ce7a
