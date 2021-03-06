package com.elena.habitTracker.areas.users.models.view;

import com.elena.habitTracker.areas.activities.entities.Activity;
import com.elena.habitTracker.areas.habits.entities.Habit;
import com.elena.habitTracker.areas.habits.util.Constants;
import com.elena.habitTracker.areas.habits.validators.EndDateAfterStartDate;

import java.util.Set;

@EndDateAfterStartDate(message = Constants.INVALID_DATE)
public class UserViewModel {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Set<String> authorities;
    private Set<Habit> habits;
    private Set<Activity> activities;

    public UserViewModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public Set<Habit> getHabits() {
        return habits;
    }

    public void setHabits(Set<Habit> habits) {
        this.habits = habits;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }

    @Override
    public String toString() {
        return String.format("%s %s (%s)",
                this.getFirstName(),
                this.getLastName(),
                this.getUsername());
    }
}
