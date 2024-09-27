package fa.training.repositories;

import fa.training.entities.Schedule;
import fa.training.enums.Role;
import fa.training.enums.Status;
import fa.training.enums.StatusInterview;
import fa.training.enums.StatusUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Schedule, Long> {
    Page<Schedule> findAll(Pageable pageable);

    // show list schedule
    @Query(
            "SELECT i.scheduleId, i.scheduleTitle, c.fullname , u.userName,i.scheduledDate, i.scheduleDateFrom, i.scheduleDateTo, i.result,i.status ,j.jobTitle " +
                    "FROM Schedule i JOIN i.interviewScheduleList s " +
                    "JOIN s.interview u " +
                    "LEFT JOIN i.candidate c " +
                    "LEFT JOIN i.job j")
    List<Object[]> findAllInterviewsSchedule();

    //read one
    @Query(
            "SELECT i.scheduleId, i.scheduleTitle, c.fullname , u.userName,i.scheduledDate, i.scheduleDateFrom, i.scheduleDateTo, i.result,i.status ,i.job.jobTitle,i.meetingId, i.note, i.location, i.recruiter.userName " +
                    "FROM Schedule i JOIN i.interviewScheduleList s " +
                    "JOIN s.interview u " +
                    "LEFT JOIN i.candidate c " +
                    "LEFT JOIN i.job j " +
                    "WHERE i.scheduleId = :scheduleId")
    List<Object[]> findScheduleByScheduleId(@Param("scheduleId") Long scheduleId);

    // Tìm kiếm bằng status hoặc interview
    @Query(
            "SELECT i.scheduleId, i.scheduleTitle, c.fullname , u.userName,i.scheduledDate, i.scheduleDateFrom, i.scheduleDateTo, i.result, i.status ,j.jobTitle " +
                    "FROM Schedule i JOIN i.interviewScheduleList s " +
                    "JOIN s.interview u " +
                    "LEFT JOIN i.candidate c " +
                    "LEFT JOIN i.job j " +
                    "WHERE (:interviewer IS NULL OR u.userId = :interviewer) " +
                    "AND (:status IS NULL OR i.status = :status )"
    )
    List<Object[]> findAllScheduleByIntereviewAndStatus(
                        @Param("interviewer") Long interviewer,
                        @Param("status") StatusInterview status
                );

    // search all
    @Query(
            "SELECT i.scheduleId, i.scheduleTitle, c.fullname , u.userName,i.scheduledDate, i.scheduleDateFrom, i.scheduleDateTo, i.result, i.status ,j.jobTitle " +
                    "FROM Schedule i JOIN i.interviewScheduleList s " +
                    "JOIN s.interview u " +
                    "LEFT JOIN i.candidate c " +
                    "LEFT JOIN i.job j " +
                    "WHERE concat(COALESCE(i.scheduleTitle, ''), COALESCE(c.fullname, ''), COALESCE(u.userName, ''), COALESCE(i.scheduledDate, ''), COALESCE(i.scheduleDateFrom, ''), COALESCE(i.scheduleDateTo, ''), COALESCE(i.result, ''), COALESCE(i.status, ''), COALESCE(j.jobTitle, ''))  " +
                    "like %:keyword% "
    )
    List<Object[]> findAllScheduleByKeyword(@Param("keyword") String keyword);


    // List interview trong search by interview
    @Query("SELECT u.userId ,u.userName FROM User u WHERE u.role = :role AND u.status = :status")
    List<Object[]> searchByInterview(@Param("role") Role role, @Param("status") StatusUser status);

    // List candidate trong select status open
    @Query("SELECT c.candidateId, c.fullname FROM Candidate c WHERE c.status = :statusCandidate" )
    List<Object[]> searchByCandidate(@Param("statusCandidate") Status statusCandidate );

    //
    // List job trong select với status open
    @Query("SELECT j.jobId, j.jobTitle FROM Job j WHERE j.status = :statusJob" )
    List<Object[]> selectByJob(@Param("statusJob") String statusJob );

    // List recruiter
    @Query("SELECT u.userId ,u.userName FROM User u WHERE u.role = :role")
    List<Object[]> selectByRecruiter(@Param("role") Role role);

    //submit result

}
