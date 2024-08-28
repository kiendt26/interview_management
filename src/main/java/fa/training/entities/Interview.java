package fa.training.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @ManyToOne
    @JoinColumn(name = "candidateId")
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "jobId")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "interviewerId")
    private User interviewer;

    private LocalDateTime scheduledDate;
    private String status;

}
