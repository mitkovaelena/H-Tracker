package com.elena.habitTracker.areas.habits.entities;

import com.elena.habitTracker.areas.activities.entities.Activity;
import com.elena.habitTracker.areas.habits.enums.FrequencyEnum;
import com.elena.habitTracker.areas.habits.enums.PriorityEnum;
import com.elena.habitTracker.areas.users.entities.User;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "habits")
public class Habit implements Comparable<Habit> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FrequencyEnum frequency;

    @Column(nullable = false, columnDefinition = "date")
    private LocalDate startDate;

    @Column(columnDefinition = "date")
    private LocalDate endDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "habit", targetEntity = Activity.class)
    private Set<Activity> activities;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PriorityEnum priority;

    private int streak;

    @Column(columnDefinition = "date")
    private LocalDate nextDueDate;

    public Habit() {
    }

    public LocalDate calculateNextDueDate() {
        LocalDate nextDueDate = this.getNextDueDate();
        do {
            switch (this.getFrequency()) {
                case YEARLY:
                    nextDueDate = nextDueDate.plusYears(this.getFrequency().getInterval());
                    break;
                case MONTHLY:
                    nextDueDate = nextDueDate.plusMonths(this.getFrequency().getInterval());
                    break;
                case MONDAY_TO_FRIDAY:
                    if (LocalDate.now().getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
                        nextDueDate = LocalDate.now().plusDays(3);
                    } else if (LocalDate.now().getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
                        nextDueDate = LocalDate.now().plusDays(2);
                    } else {
                        nextDueDate = LocalDate.now().plusDays(1);
                    }
                    break;
                default:
                    nextDueDate = nextDueDate.plusDays(this.getFrequency().getInterval());
            }
        } while (nextDueDate.isBefore(LocalDate.now()));
        if(this.getEndDate() != null && nextDueDate.isAfter(this.getEndDate())){
            nextDueDate = null;
        }
        return nextDueDate;
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

    public FrequencyEnum getFrequency() {
        return frequency;
    }

    public void setFrequency(FrequencyEnum frequency) {
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

    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }

    public PriorityEnum getPriority() {
        return priority;
    }

    public void setPriority(PriorityEnum priority) {
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

    @Override
    public int compareTo(Habit habit) {
        return this.getId().compareTo(habit.getId());
    }
}
