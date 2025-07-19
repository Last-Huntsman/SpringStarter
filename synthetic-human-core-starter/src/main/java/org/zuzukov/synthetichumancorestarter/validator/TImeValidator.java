package org.zuzukov.synthetichumancorestarter.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Constraint(validatedBy = IsoDateTimeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TImeValidator {

    String message() default "Invalid ISO 8601 date-time format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
