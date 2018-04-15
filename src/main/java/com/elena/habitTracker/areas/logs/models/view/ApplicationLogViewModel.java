package com.elena.habitTracker.areas.logs.models.view;

import java.time.LocalDateTime;

public class ApplicationLogViewModel {
    private LocalDateTime time;
    private String message;
    private String user;

    public ApplicationLogViewModel() {
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
