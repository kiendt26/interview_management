package fa.training.repositories.Interview;

import fa.training.entities.InterviewSchedule;
import fa.training.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewScheduleRepository extends JpaRepository<InterviewSchedule, Integer> {


    List<InterviewSchedule> findBySchedule(Schedule newSchedule);

    Long deleteBySchedule(Schedule schedule);
}
