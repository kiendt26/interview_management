package fa.training.utils;

import fa.training.entities.InterviewInfo;
import fa.training.entities.User;
import fa.training.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataHelper {
    @Autowired
    private UserRepository userRepository;

    public <T> T updateHelper(T currentData, T newData) {
        return newData == null || Objects.equals(currentData, newData) ? currentData : newData;
    }

    public String format(String text) {
        if (text == null) return text;
        String[] lines = text.split("\\r?\\n");
        StringBuilder formatted = new StringBuilder();
        for (String line : lines) {
            String[] words = line.trim().split("\\s+");
            for (String word : words) {
                formatted.append(word).append(" ");
            }
            formatted.append("\n");
        }
        return formatted.toString().trim();
    }

    public InterviewInfo getInterviewInfo(String interviewInfo) {
        String[] parts = interviewInfo.split(" \\| Interviewer: ", 2);
        if (parts.length < 2) {
            return null;
        }

        String title = parts[0];
        List<String> interviewers = Arrays.asList(parts[1].split(",\\s*"));

        List<User> users = new ArrayList<>();
        for (String user : interviewers) {
            users.add(userRepository.findByUserName(user));
        }

        InterviewInfo info = InterviewInfo.builder()
                .title(title)
                .users(users)
                .build();

        return info;
    }
}
