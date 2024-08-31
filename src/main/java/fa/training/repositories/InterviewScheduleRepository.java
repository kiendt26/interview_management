package fa.training.repositories;

import fa.training.entities.InterviewSchedule;
import fa.training.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public interface InterviewScheduleRepository extends JpaRepository<InterviewSchedule, Integer> {

    @Query( value = "SELECT u.* FROM Interview i " +
            "INNER JOIN InterviewSchedule s ON i.interviewId = s.Interview_Id " +
            "INNER JOIN Users u ON s.User_Id = u.userId " , nativeQuery = true)
    List<InterviewSchedule> findName();
}
