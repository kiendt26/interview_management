package fa.training.entities;

import fa.training.enums.Department;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long candidateId;

    private String fullName;
    private String email;
    private String phone;
    private String status;



}