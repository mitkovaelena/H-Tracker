package org.softuni.habitTracker.domain.models.view;

import org.softuni.habitTracker.domain.entities.User;
import org.softuni.habitTracker.util.Constants;
import org.softuni.habitTracker.util.validators.EndDateAfterStartDate;
import org.softuni.habitTracker.util.validators.PresentOrFuture;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

@EndDateAfterStartDate(message = Constants.INVALID_DATE)
public class HabitViewDTO {
    private Long id;
    private String title;
    private String frequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate nextDueDate;
    private String priority;
    private int streak;
    public HabitViewDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public LocalDate getNextDueDate() {
        return nextDueDate;
    }

    public void setNextDueDate(LocalDate nextDueDate) {
        this.nextDueDate = nextDueDate;
    }
}
