package com.elena.habitTracker.areas.habits.validators;

import com.elena.habitTracker.areas.habits.util.Constants;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = PresentOrFutureValidator.class)
public @interface PresentOrFuture {
    String message() default Constants.INVALID_DATE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}