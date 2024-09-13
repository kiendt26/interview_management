package fa.training.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class InterviewSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "Schedule_Id")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "Interview_Id")
    private User interview;

}
