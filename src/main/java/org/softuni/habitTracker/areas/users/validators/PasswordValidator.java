package org.softuni.habitTracker.areas.users.validators;

import org.softuni.habitTracker.areas.users.util.Constants;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PasswordValidator implements ConstraintValidator<Password, String> {

    private boolean containsDigit;
    private boolean containsUpperCase;
    private boolean containsLowerCase;
    private boolean containsSpecialSymbols;
    private int minLength;
    private int maxLength;

    @Override
    public void initialize(Password password) {
        this.containsDigit = password.containsDigit();
        this.containsUpperCase = password.containsUpperCase();
        this.containsLowerCase = password.containsLowerCase();
        this.containsSpecialSymbols = password.containsSpecialSymbols();
        this.minLength = password.minLength();
        this.maxLength = password.maxLength();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password.length() < this.minLength || password.length() > this.maxLength) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    String.format(Constants.PASSWORD_LENGTH, this.minLength, this.maxLength))
                    .addConstraintViolation();
            return false;
        }

        Pattern pattern = Pattern.compile("[a-z]");
        Matcher matcher = pattern.matcher(password);

        if (this.containsLowerCase && !matcher.find()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(Constants.PASSWORD_LOWERCASE_CHARACTER)
                    .addConstraintViolation();
            return false;
        }

        pattern = Pattern.compile("[A-Z]");
        matcher = pattern.matcher(password);

        if (this.containsUpperCase && !matcher.find()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(Constants.PASSWORD_UPPERCASE_CHARACTER)
                    .addConstraintViolation();
            return false;
        }

        pattern = Pattern.compile("[!@#$%^&*()_+=]");
        matcher = pattern.matcher(password);

        if (this.containsSpecialSymbols && !matcher.find()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(Constants.PASSWORD_SPECIAL_SYMBOL)
                    .addConstraintViolation();
            return false;
        }

        pattern = Pattern.compile("[\\d+]");
        matcher = pattern.matcher(password);

        if (this.containsDigit && !matcher.find()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(Constants.PASSWORD_DIGIT)
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
