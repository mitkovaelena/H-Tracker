package org.softuni.habitTracker.util.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EndDateAfterStartDateValidator implements ConstraintValidator<EndDateAfterStartDate, Object> {
    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            Date startDate = getFieldValue(object, "startDate");
            Date endDate = getFieldValue(object, "endDate");
            return endDate == null || endDate.after(startDate);
        } catch (Exception e) {
            return false;
        }
    }

    private Date getFieldValue(Object object, String fieldName) throws Exception {
        Class<?> clazz = object.getClass();
        Field date = clazz.getDeclaredField(fieldName);
        date.setAccessible(true);
        return (Date) date.get(object);
    }
}