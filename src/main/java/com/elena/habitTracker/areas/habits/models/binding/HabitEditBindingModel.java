package com.elena.habitTracker.areas.habits.models.binding;

import com.elena.habitTracker.areas.habits.util.Constants;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class HabitEditBindingModel {
    private Long id;

    @NotNull
    @NotEmpty(message = Constants.TITLE_NOT_EMPTY)
    private String title;

    @NotNull(message = Constants.FREQUENCY_NOT_EMPTY)
    private String frequency;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull(message = Constants.PRIORITY_NOT_EMPTY)
    private String priority;

    public HabitEditBindingModel() {
    }

    public HabitEditBindingModel(String title, String frequency, String priority) {
        this.title = title;
        this.frequency = frequency;
        this.priority = priority;
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
}
