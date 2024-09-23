package fa.training.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ResultInterviewConvert implements AttributeConverter<ResultInterview, String> {

    @Override
    public String convertToDatabaseColumn(ResultInterview resultInterview) {
        if (resultInterview == null) {
            return null;
        }
        System.out.println("Converting " + resultInterview + " to " + resultInterview.getDisplayName());
        return resultInterview.getDisplayName();
    }

    @Override
    public ResultInterview convertToEntityAttribute(String displayName) {
        if (displayName == null) {
            return null;
        }

        for (ResultInterview result : ResultInterview.values()) {
            if (result.getDisplayName().equals(displayName)) {
                return result;
            }
        }

        throw new IllegalArgumentException("Unknown displayName: " + displayName);
    }
}
