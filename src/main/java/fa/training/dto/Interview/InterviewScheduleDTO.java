package fa.training.dto.Interview;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class InterviewScheduleDTO {
    private String scheduleTitle;
    private Long candidateId;
    private LocalDate scheduleDate;
    private LocalTime scheduleDateFrom;
    private LocalTime scheduleDateTo;
    private String note;
    private Long jobId;
    private String location;
    private Long recruiterId;
    private Long meetingId;
    private List<Long> interviewIds;
}
