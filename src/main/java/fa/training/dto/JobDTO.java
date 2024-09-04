package fa.training.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class JobDTO {


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
    private String[] skillsDTO;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @NotEmpty(message = "Benefit must not empty")
    private String[] benefitDTO;

    @NotEmpty(message = "Level must not empty")
    private String[] levelDTO;
    private String description;
    private Boolean status;


}
