package com.elena.habitTracker.areas.habits.models.view;

import com.elena.habitTracker.areas.habits.util.Constants;
import com.elena.habitTracker.areas.habits.validators.EndDateAfterStartDate;

import java.time.LocalDate;

@EndDateAfterStartDate(message = Constants.INVALID_DATE)
public class HabitViewModel implements Comparable<HabitViewModel>{
    private Long id;
    private String title;
    private String frequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate nextDueDate;
    private String priority;
    private int streak;
    private Long userId;
    private String username;

    public HabitViewModel() {
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int compareTo(HabitViewModel other) {
        return ((int)this.getPriority().charAt(0) + (int)this.getPriority().charAt(1))
                - ((int)other.getPriority().charAt(0) + (int)other.getPriority().charAt(1));
    }
}
