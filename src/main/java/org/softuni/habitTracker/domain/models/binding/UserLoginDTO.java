package org.softuni.habitTracker.domain.models.binding;

import org.softuni.habitTracker.util.validators.Email;
import org.softuni.habitTracker.util.validators.Password;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserLoginDTO {
    @NotNull
    @NotEmpty(message = "Username must not be empty")
    private String username;

    @NotNull
    @NotEmpty(message = "Password must not be empty")
    private String password;

    public UserLoginDTO() {
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

}
