package org.softuni.habitTracker.domain.entities;

import org.softuni.habitTracker.util.validators.Email;
import org.softuni.habitTracker.util.validators.Password;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String firstName;

    private String lastName;

   // private Date registeredOn;

   // private Date lastTimeLoggedIn;

   // private Integer age;

   // private Boolean isDeleted;

    // profile picture?

    @OneToMany(mappedBy = "user", targetEntity = Habit.class)
    private Set<Habit> habits;

    @OneToMany(mappedBy = "user", targetEntity = Log.class)
    private Set<Habit> logs;
//
//    @ManyToMany
//    @JoinTable(name = "users_friends",
//            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "friend_id", referencedColumnName = "id"))
//    private List<User> friends;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
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

    public Set<Habit> getHabits() {
        return habits;
    }

    public void setHabits(Set<Habit> habits) {
        this.habits = habits;
    }

    public Set<Habit> getLogs() {
        return logs;
    }

    public void setLogs(Set<Habit> logs) {
        this.logs = logs;
    }
}
