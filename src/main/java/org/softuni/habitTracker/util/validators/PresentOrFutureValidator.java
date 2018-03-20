package org.softuni.habitTracker.util.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PresentOrFutureValidator implements ConstraintValidator<PresentOrFuture, Date> {

    @Override
    public boolean isValid(Date date, ConstraintValidatorContext arg1) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date yesterday = new Date(new Date().getTime() - 24 * 3600 * 1000 );
        return date == null || date.after(yesterday);
    }
}