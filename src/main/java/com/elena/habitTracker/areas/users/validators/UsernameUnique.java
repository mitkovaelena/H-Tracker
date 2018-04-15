package com.elena.habitTracker.areas.users.validators;

import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Component
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameUniqueValidator.class)
public @interface UsernameUnique {
    String message() default "Username is already taken.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
