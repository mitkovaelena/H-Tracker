package org.softuni.habitTracker.domain.models.binding;

import org.softuni.habitTracker.util.validators.Email;
import org.softuni.habitTracker.util.validators.Password;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserRegisterDTO {
    @NotNull
    @Size(min = 4, max = 30, message = "Username must be between 4 and 30 characters long.")
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

    @Email(message = "Invalid email.")
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
