package org.softuni.habitTracker.domain.entities;

import org.softuni.habitTracker.util.enums.FrequencyEnum;
import org.softuni.habitTracker.util.enums.PriorityEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "habits")
public class Habit {
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

    @Column(nullable = false, columnDefinition = "date")
    private LocalDate nextDueDate;

    public Habit() {
    }

    public LocalDate calculateNextDueDate() {
        switch (this.getFrequency()){
            case YEARLY:
                return LocalDate.now().plusYears(this.getFrequency().getInterval());
            case MONTHLY:
                return this.getNextDueDate().plusMonths(this.getFrequency().getInterval());
            case MONDAY_TO_FRIDAY:
                if(LocalDate.now().getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
                    return this.getNextDueDate().plusDays(3);
                } else if (LocalDate.now().getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
                    return this.getNextDueDate().plusDays(2);
                }
            default:
                return this.getNextDueDate().plusDays(this.getFrequency().getInterval());
        }
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
}
