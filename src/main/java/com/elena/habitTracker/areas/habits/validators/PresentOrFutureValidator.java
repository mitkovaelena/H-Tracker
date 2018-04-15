package com.elena.habitTracker.areas.habits.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class PresentOrFutureValidator implements ConstraintValidator<PresentOrFuture, LocalDate> {

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext arg1) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return date == null || date.isAfter(yesterday);
    }
}