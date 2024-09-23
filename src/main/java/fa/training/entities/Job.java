package fa.training.entities;

import fa.training.enums.Benefit;
import fa.training.enums.Level;
import fa.training.enums.Skills;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Job implements Serializable {
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;


    @NotBlank(message = "Job Title must not be blank")
    private String jobTitle;

    @Future(message = "Date must be in future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Positive(message = "Salary must be positive")
    private Double salaryFrom;

    @Positive(message = "Salary must be positive")
    private Double salaryTo;

    private String workingAddress;

    @NotEmpty(message = "Skill must not empty")
    private String skills;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @NotEmpty(message = "Benefit must not empty")
    private String benefit;

    @NotEmpty(message = "Level must not empty")
    private String level;
    private String description;
    private String status;


}
