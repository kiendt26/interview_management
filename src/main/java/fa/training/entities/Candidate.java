package fa.training.entities;

import fa.training.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Candidate implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long candidateId;

    private String fullName;
    private String email;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Status status;
}