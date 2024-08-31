package fa.training.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interviewId;

    @Column(name = "Interview_Title")
    private String interviewTitle;

    @Column(name = "Schedule_Date")
    private LocalDate scheduledDate;

    private String location;

    @Column(name = "Meeting_ID")
    private String meetingId;

    private String note;

    @Column(name = "Schedule_Date_From")
    private LocalTime sechduleDateFrom;

    @Column(name = "Schedule_Date_To")
    private LocalTime sechduleDateTo;
    private String status;

    @ManyToOne
    @JoinColumn(name = "Candidate_Id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "Job_Id")
    private Job job;




}
