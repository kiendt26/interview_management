package fa.training.dto;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobDTO {


    private Long jobId;


    @NotBlank(message = "Job Title must not be blank")
    private String jobTitle;

    @Future(message = "Date must be in future")
    @NotNull(message = "Start Date must not be blank")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDate startDate;

    @Positive(message = "Salary must be positive")

    private Double salaryFrom;

    @Positive(message = "Salary must be positive")
    @NotNull(message = "Salary must not be blank")
    private Double salaryTo;

    private String workingAddress;

    @NotEmpty(message = "Skill must not empty")
    private String[] skillsDTO;

    @NotNull(message = "End Date must not be blank")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDate endDate;
    
    @NotEmpty(message = "Benefit must not empty")
    private String[] benefitDTO;


    @NotEmpty(message = "Level must not empty")
    private String[] levelDTO;

    private String description;
    private String status;

    public JobDTO(String jobTitle, @NotEmpty(message = "Skill must not empty") String[] skillsDTO, @NotEmpty(message = "Level must not empty") String[] levelDTO) {
        this.jobTitle = jobTitle;
        this.skillsDTO = skillsDTO;
        this.levelDTO = levelDTO;
    }
}
