package fa.training.repositories;

import fa.training.entities.Interview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    Page<Interview> findAll(Pageable pageable);

//    Page<Interview> findByTitleLike(String title, Pageable pageable);
}
