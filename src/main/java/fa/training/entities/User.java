package fa.training.entities;

import fa.training.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;
    private String passwordHash;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String email;
    private String passApp;

}
