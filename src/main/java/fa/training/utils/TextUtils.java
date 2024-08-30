package fa.training.utils;

import org.springframework.stereotype.Component;

@Component
public class TextUtils {
    public String format(String text) {
        String formatted = "";
        String[] words = text.trim().split("\\s+");
        for(String run : words) {
            formatted = formatted + run + "\s";
        }
        return formatted.trim();
    }
}
