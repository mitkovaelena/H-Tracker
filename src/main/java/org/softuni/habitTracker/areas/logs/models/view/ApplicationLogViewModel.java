package org.softuni.habitTracker.areas.logs.models.view;

import org.softuni.habitTracker.areas.users.entities.User;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
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
