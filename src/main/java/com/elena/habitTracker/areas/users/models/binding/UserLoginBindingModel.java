package com.elena.habitTracker.areas.users.models.binding;

import com.elena.habitTracker.areas.users.util.Constants;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserLoginBindingModel {
    @NotNull
    @NotEmpty(message = Constants.USERNAME_NOT_EMPTY)
    private String username;

    @NotNull
    @NotEmpty(message = Constants.PASSWORD_NOT_EMPTY)
    private String password;

    public UserLoginBindingModel() {
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
