package fa.training.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class InterviewSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "Interview_Id")
    private Interview interview;

    @ManyToOne
    @JoinColumn(name = "User_Id")
    private User user;

}
