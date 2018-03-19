package org.softuni.habitTracker.domain.models.binding;

import org.softuni.habitTracker.util.Constants;
import org.softuni.habitTracker.util.validators.EqualFields;
import org.softuni.habitTracker.util.validators.Password;
import org.softuni.habitTracker.util.validators.UsernameUnique;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@EqualFields(baseField = "password", matchField = "confirmPassword", message = Constants.PASSWORDS_MISMATCH)
public class UserRegisterDTO {
    @NotNull
    @Size(min = 4, max = 30, message = Constants.USERNAME_LENGTH)
    @UsernameUnique
    private String username;

    @NotNull
    @Password(minLength = 6,
            maxLength = 50,
            containsDigit = false,
            containsLowerCase = false,
            containsUpperCase = false,
            containsSpecialSymbols = false)
    private String password;

    private String confirmPassword;

    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9.-_]*[A-Za-z0-9]@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*\\w+$", message = Constants.INVALID_EMAIL)
    @NotNull
    private String email;

    private String firstName;

    private String lastName;

    // private Date registeredOn;

    // private Integer age;

    // profile picture?


    public UserRegisterDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
