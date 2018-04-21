package com.elena.habitTracker.areas.activities.models.binding;

import com.elena.habitTracker.areas.habits.entities.Habit;
import com.elena.habitTracker.areas.users.entities.User;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public class ActivityAddBindingModel {
    @NotNull
    @PastOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private User user;

    private Habit habit;

    public ActivityAddBindingModel() {
    }

    public ActivityAddBindingModel(@NotNull @PastOrPresent LocalDate date, User user, Habit habit) {
        this.date = date;
        this.user = user;
        this.habit = habit;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Habit getHabit() {
        return habit;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
    }

}
