package org.softuni.habitTracker.areas.activities.models.view;

import org.softuni.habitTracker.areas.habits.entities.Habit;
import org.softuni.habitTracker.areas.habits.util.Constants;
import org.softuni.habitTracker.areas.habits.validators.EndDateAfterStartDate;

import java.time.LocalDate;

@EndDateAfterStartDate(message = Constants.INVALID_DATE)
public class ActivityViewModel {
    private Long id;
    private LocalDate date;
    private Habit habit;

    public ActivityViewModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Habit getHabit() {
        return habit;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
    }
}
