package com.elena.habitTracker.areas.activities.entities;

import com.elena.habitTracker.areas.habits.entities.Habit;
import com.elena.habitTracker.areas.users.entities.User;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "activities")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "date")
    private LocalDate date;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "habit_id", referencedColumnName = "id")
    private Habit habit;

    public Activity() {
    }

    public Activity(LocalDate date, User user, Habit habit) {
        this.date = date;
        this.user = user;
        this.habit = habit;
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
