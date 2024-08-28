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
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long offerId;

    @ManyToOne
    @JoinColumn(name = "candidateId")
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "jobId")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "offeredById")
    private User offeredBy;

    private String status;
    private LocalDateTime offerDate;

}
