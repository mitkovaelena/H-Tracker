package org.softuni.habitTracker.domain.entities;

import org.softuni.habitTracker.util.enums.HabitFrequencyEnum;

import javax.persistence.*;
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
    private HabitFrequencyEnum frequency;

    @Column(nullable = false)
    private Date startDate;

    private Date endDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "habit", targetEntity = Log.class)
    private Set<Habit> logs;

//    @Column(nullable = false)
//    @Enumerated(EnumType.STRING)
//    private Priority priority;

    // Category category


    public Habit() {
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

    public HabitFrequencyEnum getFrequency() {
        return frequency;
    }

    public void setFrequency(HabitFrequencyEnum frequency) {
        this.frequency = frequency;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Habit> getLogs() {
        return logs;
    }

    public void setLogs(Set<Habit> logs) {
        this.logs = logs;
    }
}
