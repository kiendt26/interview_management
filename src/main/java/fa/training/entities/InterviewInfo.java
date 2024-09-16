package fa.training.entities;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterviewInfo {
    private String title;
    private List<User> users = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder format = new StringBuilder();
        for(User user : users){
            format.append(user.getUserName()).append(", ");
        }
        return title + ", " + format;
    }
}
