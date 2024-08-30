package fa.training.utils;

import org.springframework.stereotype.Component;

@Component
public class DataHelper {
    public <T> T updateHelper(T currentData, T newData) {
        return newData == null ? currentData : newData;
    }

    public String format(String text) {
        if(text == null) return text;
        String[] lines = text.split("\\r?\\n");
        StringBuilder formatted = new StringBuilder();
        for(String line : lines) {
            String[] words = line.trim().split("\\s+");
            for (String word : words) {
                formatted.append(word).append(" ");
            }
            formatted.append("\n");
        }
        return formatted.toString().trim();
    }
}
