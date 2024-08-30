package fa.training.utils;

import org.springframework.stereotype.Component;

@Component
public class DataHelper {
    public <T> T updateHelper(T currentData, T newData) {
        return newData == null ? currentData : newData;
    }

    public String format(String text) {
        if(text == null) return text;
        String formatted = "";
        String[] words = text.trim().split("\\s+");
        for(String run : words) {
            formatted = formatted + run + "\s";
        }
        return formatted.trim();
    }
}
