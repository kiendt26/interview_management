package fa.training;

import fa.training.entities.Schedule;
import fa.training.repositories.Interview.InterviewScheduleRepository;
import fa.training.services.InterviewServce;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ScheduleManagementApplicationTests {
    @Autowired
    private InterviewScheduleRepository interviewScheduleRepository;
    @Autowired
    private InterviewServce interviewServce;
    @Test
    void contextLoads() {
        Schedule newSchedule = new Schedule();
        newSchedule.setScheduleTitle("123");
        List<Long> interviewIds = new ArrayList<>();
        interviewIds.add(1L);
        interviewIds.add(2L);
        interviewServce.createNewSchedule(newSchedule, interviewIds);

    }

}
