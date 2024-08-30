package fa.training.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimeRange.class)
public @interface ValidOfferTime {
    String message() default "Invalid time, offer from must < offer to";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String offerFrom();
    String offerTo();
}
