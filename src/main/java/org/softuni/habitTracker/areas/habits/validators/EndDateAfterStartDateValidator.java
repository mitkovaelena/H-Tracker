package org.softuni.habitTracker.areas.habits.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDate;

public class EndDateAfterStartDateValidator implements ConstraintValidator<EndDateAfterStartDate, Object> {
    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            LocalDate startDate = getFieldValue(object, "startDate");
            LocalDate endDate = getFieldValue(object, "endDate");
            return endDate == null || endDate.isAfter(startDate);
        } catch (Exception e) {
            return false;
        }
    }

    private LocalDate getFieldValue(Object object, String fieldName) throws Exception {
        Class<?> clazz = object.getClass();
        Field date = clazz.getDeclaredField(fieldName);
        date.setAccessible(true);
        return (LocalDate) date.get(object);
    }
}