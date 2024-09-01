package fa.training.entities;

import fa.training.enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidate_id")
    private Long candidateId;

    @NotBlank(message = "Full name is required")
    @Column(name = "fullname", nullable = false)
    private String fullname;

    @Past(message = "Date of birth must be in the past")
    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private Date dob;

    @Pattern(regexp = "\\d{10,15}", message = "Phone number must be between 10 and 15 digits")
    @Column(name = "phone", length = 15)
    private String phone;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "address")
    private String address;

    @NotNull(message = "Gender is required")
    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "attachment", length = 255)
    private String attachment;

    @NotNull(message = "Skill is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "skill")
    private Skills skill;

    @NotNull(message = "Position is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    private CurrentPosition position;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Min(value = 0, message = "Years of experience must be 0 or more")
    @Column(name = "year_of_exp")
    private Integer yearOfExp;

    @NotNull(message = "Highest level is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "highest_level")
    private HighestLevel highestLevel;


}