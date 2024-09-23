package fa.training.entities;

import fa.training.enums.Benefit;
import fa.training.enums.Level;
import fa.training.enums.Skills;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Job implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;


    @NotBlank(message = "Job Title must not be blank")
    private String jobTitle;

    @Future(message = "Date must be in future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private LocalDate startDate;

    @Positive(message = "Salary must be positive")
    private Double salaryFrom;

    @Positive(message = "Salary must be positive")
    private Double salaryTo;

    private String workingAddress;

    @NotEmpty(message = "Skill must not empty")
    private String skills;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private LocalDate endDate;

    @NotEmpty(message = "Benefit must not empty")
    private String benefit;

    @NotEmpty(message = "Level must not empty")
    private String level;
    private String description;
    private String status;


}
