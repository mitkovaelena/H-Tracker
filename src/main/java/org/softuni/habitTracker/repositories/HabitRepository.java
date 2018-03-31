package org.softuni.habitTracker.repositories;

import org.softuni.habitTracker.domain.entities.Habit;
import org.softuni.habitTracker.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {
    List<Habit> findAllByUser(User user);

    List<Habit> findAllByUserAndNextDueDate (User user, LocalDate date);
}