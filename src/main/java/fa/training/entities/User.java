package fa.training.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "USERS")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;
    private String passwordHash;
    private String role;
    private String email;

    @OneToMany(mappedBy = "approvalBy")
    private List<Offer> offers = new ArrayList<>();

}
