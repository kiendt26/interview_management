package fa.training.entities;

import fa.training.enums.*;
import jakarta.persistence.*;
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

    @Column(name = "fullname", nullable = false)
    private String fullname;

    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private Date dob;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "attachment", length = 255)
    private String attachment;

    @Enumerated(EnumType.STRING)
    @Column(name = "skill")
    private Skills skill;

    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    private CurrentPosition position;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "year_of_exp")
    private Integer yearOfExp;

    @Enumerated(EnumType.STRING)
    @Column(name = "highest_level")
    private HighestLevel highestLevel;



}