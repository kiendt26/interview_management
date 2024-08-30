package fa.training.dto;

import fa.training.enums.ContractType;
import fa.training.enums.CurrentPosition;
import fa.training.enums.Department;
import fa.training.enums.Level;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class OfferDTO {
    private Long offerId;
    private Long candidateId;
    private CurrentPosition position;
    private String interviewInfo;
    private LocalDate contractFrom;
    private LocalDate contractTo;
    private String interviewNote;
    private ContractType contractType;
    private Level level;
    private Department department;
    private String recruiterOwner;
    private LocalDate dueDate;
    private BigDecimal basicSalary;
    private String note;
}
