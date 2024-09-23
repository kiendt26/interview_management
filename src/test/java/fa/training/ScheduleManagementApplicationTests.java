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
    void testSendMail(){
//        interviewServce.sendEmail1();
    }

}
