package fa.training.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.time.LocalDate;

public class TimeRange implements ConstraintValidator<ValidOfferTime, Object> {
    private String offerFrom;
    private String offerTo;

    @Override
    public void initialize(ValidOfferTime constraintAnnotation) {
        offerFrom = constraintAnnotation.offerFrom();
        offerTo = constraintAnnotation.offerTo();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try{
            Field from = value.getClass().getDeclaredField(offerFrom);
            Field to = value.getClass().getDeclaredField(offerTo);

            from.setAccessible(true);
            to.setAccessible(true);

            LocalDate startTime = (LocalDate)from.get(value);
            LocalDate endTime = (LocalDate)to.get(value);

            if(startTime == null  && endTime == null) return true;

            if(!startTime.isAfter(endTime)) return false;

            return true;
        } catch (Exception e){
            return false;
        }
    }
}
