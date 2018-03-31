package org.softuni.habitTracker.domain.models.binding;

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
public class HabitAddDTO {
    @NotNull
    @NotEmpty(message = Constants.TITLE_NOT_EMPTY)
    private String title;

    @NotNull(message = Constants.FREQUENCY_NOT_EMPTY)
    private String frequency;

    @NotNull(message = Constants.DATE_NOT_EMPTY)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PresentOrFuture
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull(message = Constants.PRIORITY_NOT_EMPTY)
    private String priority;

    private User user;

    public HabitAddDTO() {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
