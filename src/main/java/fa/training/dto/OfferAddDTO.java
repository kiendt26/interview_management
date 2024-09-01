package fa.training.dto;

import fa.training.enums.ContractType;
import fa.training.enums.CurrentPosition;
import fa.training.enums.Department;
import fa.training.enums.Level;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NotNull
public class OfferAddDTO {
    private Long candidateId;
    private ContractType contractType;
    private CurrentPosition position;
    private Level level;
    private Long approver;
    private Department department;
    private String interviewNote;
    private Long recruiterOwnerId;
    private LocalDate contractFrom;
    private LocalDate contractTo;
    private LocalDate dueDate;
    private BigDecimal basicSalary;
    private String note;
}
