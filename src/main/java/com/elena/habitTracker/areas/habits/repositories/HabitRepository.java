package com.elena.habitTracker.areas.habits.repositories;

import com.elena.habitTracker.areas.habits.entities.Habit;
import com.elena.habitTracker.areas.users.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {
    List<Habit> findAllByUser(User user);

    Page<Habit> findAllByUser(User user, Pageable pageable);

    List<Habit> findAllByUserAndNextDueDate(User user, LocalDate date);
}