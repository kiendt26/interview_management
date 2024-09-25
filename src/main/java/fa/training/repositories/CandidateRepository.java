package fa.training.repositories;

import fa.training.entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {


    @Query("SELECT c FROM Candidate c WHERE " +
            "(LOWER(c.fullname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.phone) LIKE LOWER(CONCAT('%', :keyword, '%'))) ")
    List<Candidate> findByKeyword(@Param("keyword") String keyword);

    List<Candidate> findByStatus(String status);

    @Query("SELECT c FROM Candidate c WHERE " +
            "(LOWER(TRIM(c.fullname)) LIKE LOWER(CONCAT('%', TRIM(:keyword), '%')) OR " +
            "LOWER(TRIM(c.email)) LIKE LOWER(CONCAT('%', TRIM(:keyword), '%')) OR " +
            "LOWER(TRIM(c.phone)) LIKE LOWER(CONCAT('%', TRIM(:keyword), '%'))) " +
            "AND (:status IS NULL OR c.status = :status)")
    List<Candidate> findByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") String status);


}
