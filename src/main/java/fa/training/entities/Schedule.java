package fa.training.entities;

import fa.training.enums.ResultInterview;
import fa.training.enums.ResultInterviewConvert;
import fa.training.enums.StatusInterview;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Schedule {
    @Id
    @Column(name = "Schedule_Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @NotBlank(message = "*Schedule title can't be null")
    @Column(name = "Schedule_Title")
    private String scheduleTitle;

    @NotNull(message = "*Scheduled date cannot be null")
//    @FutureOrPresent(message = "*Scheduled date must be today or in the future")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Column(name = "Schedule_Date")
    private LocalDate scheduledDate;

    private String location;

    @Column(name = "Meeting_ID")
    private String meetingId;

    private String note;

    @NotNull(message = "*Schedule start time cannot be null")
    @DateTimeFormat(pattern = "HH:mm:ss")
    @Column(name = "Schedule_Date_From")
    private LocalTime scheduleDateFrom;

    @NotNull(message = "*Schedule end time cannot be null")
    @DateTimeFormat(pattern = "HH:mm:ss")
    @Column(name = "Schedule_Date_To")
    private LocalTime scheduleDateTo;

    @AssertTrue(message = "*Time To must >= Time From")
    public boolean isTimeValid() {
        return scheduleDateFrom != null && scheduleDateTo != null
                && !scheduleDateFrom.isAfter(scheduleDateTo);
    }

    @Enumerated(EnumType.STRING)
    private StatusInterview status;

    @Convert(converter = ResultInterviewConvert.class)
    private ResultInterview result;

    @ManyToOne
    @JoinColumn(name = "Candidate_Id")
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "Recuiter_Id")
    private User recruiter;

    @ManyToOne
    @JoinColumn(name = "Job_Id")
    private Job job;

    @OneToMany(mappedBy = "schedule")
    private List<InterviewSchedule> interviewScheduleList = new ArrayList<>();




}
