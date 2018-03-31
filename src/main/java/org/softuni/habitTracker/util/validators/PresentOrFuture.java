package org.softuni.habitTracker.util.validators;

import org.softuni.habitTracker.util.Constants;

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