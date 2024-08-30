package fa.training.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fa.training.enums.*;
import fa.training.validation.ValidOfferTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ValidOfferTime(offerFrom = "contractFrom", offerTo = "contractTo")
public class Offer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long offerId;

    @ManyToOne
    @JoinColumn(name = "candidateId")
    @NotNull
    @JsonIgnore
    private Candidate candidate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private CurrentPosition position;

    @NotNull
    private String interviewInfo;

    @NotNull
    private LocalDate contractFrom;

    @NotNull
    private LocalDate contractTo;

    @Column(columnDefinition = "TEXT")
    @ColumnDefault("'N/A'")
    @NotNull
    private String interviewNote;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ContractType contractType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Level level;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Department department;

    @NotNull
    private String recruiterOwner;

    @NotNull
    private LocalDate dueDate;

    @NotNull
    private BigDecimal basicSalary;

    @Column(columnDefinition = "TEXT")
    @ColumnDefault("'N/A'")
    @NotNull
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @ColumnDefault("'WAITING'")
    private Status status;

    private LocalDate offerDate;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "approver", referencedColumnName = "userId")
    private User approvalBy;

    public boolean noDataInRequired(){
        return  Objects.isNull(candidate) ||
                Objects.isNull(position) ||
                Objects.isNull(interviewInfo) ||
                Objects.isNull(contractFrom) ||
                Objects.isNull(contractTo) ||
                Objects.isNull(contractType) ||
                Objects.isNull(level) ||
                Objects.isNull(department) ||
                Objects.isNull(recruiterOwner) ||
                Objects.isNull(dueDate) ||
                Objects.isNull(basicSalary);
    }
}
