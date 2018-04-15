package com.elena.habitTracker.areas.users.validators;

import com.elena.habitTracker.areas.users.services.UserService;
import com.elena.habitTracker.areas.users.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UsernameUniqueValidator implements ConstraintValidator<UsernameUnique, String> {
    private final UserService userService;

    @Autowired
    public UsernameUniqueValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(UsernameUnique constraintAnnotation) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null) {
            return false;
        }
        if (userService.getByUsername(username) != null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(Constants.USERNAME_TAKEN)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}