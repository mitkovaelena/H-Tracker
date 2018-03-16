package org.softuni.habitTracker.util.validators;

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
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if (password.length() <  this.minLength || password.length() > this.maxLength) {
            return false;
        }

        Pattern pattern = Pattern.compile("[a-z]");
        Matcher matcher = pattern.matcher(password);

        if (this.containsLowerCase && !matcher.find()) {
            return false;
        }

        pattern = Pattern.compile("[A-Z]");
        matcher = pattern.matcher(password);

        if (this.containsUpperCase && !matcher.find()) {
            return false;
        }

        pattern = Pattern.compile("[!@#$%^&*()_+=]");
        matcher = pattern.matcher(password);

        if (this.containsSpecialSymbols && !matcher.find()) {
            return false;
        }

        pattern = Pattern.compile("[\\d+]");
        matcher = pattern.matcher(password);

        if (this.containsDigit && !matcher.find()) {
            return false;
        }

        return true;
    }
}
