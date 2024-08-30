package fa.training.entities;

import fa.training.enums.CurrentPosition;
import fa.training.enums.Department;
import fa.training.enums.Level;
import fa.training.enums.OfferStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Offer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long offerId;

    @ManyToOne
    @JoinColumn(name = "candidateId")
    private Candidate candidate;

    @Enumerated(EnumType.STRING)
    private CurrentPosition position;

    private String interviewInfo;

    private LocalDate contactFrom;

    private LocalDate contactTo;

    private String interviewNote;

    private String contactType;

    @Enumerated(EnumType.STRING)
    private Level level;

    @Enumerated(EnumType.STRING)
    private Department department;

    private String recruiterOwner;

    private LocalDate dueDate;

    private BigDecimal basicSalary;

    private String note;

    @OneToMany(mappedBy = "users")
    private List<User> approvalBy = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OfferStatus status;

    private LocalDateTime offerDate;
}
