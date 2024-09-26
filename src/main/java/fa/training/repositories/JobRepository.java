package fa.training.repositories;

import fa.training.dto.JobDTO;
import fa.training.entities.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job,Long> {
    @Query("SELECT j Job " +
            "FROM Job j WHERE " +
            "(LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.skills) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.level) LIKE LOWER(CONCAT('%', :keyword, '%'))) ")
    Page<JobDTO> findByKeyword(@Param("keyword") String keyword,Pageable pageable);

    Page<JobDTO> findByStatus(String status, Pageable pageable);

    @Query("SELECT j FROM Job j WHERE " +
            "(LOWER(TRIM(j.jobTitle)) LIKE LOWER(CONCAT('%', TRIM(:keyword), '%')) OR " +
            "LOWER(TRIM(j.skills)) LIKE LOWER(CONCAT('%', TRIM(:keyword), '%')) OR " +
            "LOWER(TRIM(j.level)) LIKE LOWER(CONCAT('%', TRIM(:keyword), '%'))) " +
            "AND (:status IS NULL OR j.status = :status)")
    Page<JobDTO> findByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") String status, Pageable pageable);

//    @Query("SELECT new fa.training.dto.JobDTO (j.jobId, j.jobTitle, j.skills, j.level, j.status) " +
//            "FROM Job j " +
//            "WHERE (LOWER(TRIM(j.jobTitle)) LIKE LOWER(CONCAT('%', TRIM(:keyword), '%')) OR " +
//            "LOWER(TRIM(j.skills)) LIKE LOWER(CONCAT('%', TRIM(:keyword), '%')) OR " +
//            "LOWER(TRIM(j.level)) LIKE LOWER(CONCAT('%', TRIM(:keyword), '%'))) " +
//            "AND (:status IS NULL OR j.status = :status)")
//    Page<JobDTO> findByKeywordAndStatus(@Param("keyword") String keyword,
//                                        @Param("status") String status,
//                                        Pageable pageable);

}

