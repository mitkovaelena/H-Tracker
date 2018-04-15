package com.elena.habitTracker.areas.activities.repositories;

import com.elena.habitTracker.areas.activities.entities.Activity;
import com.elena.habitTracker.areas.users.entities.User;
import com.elena.habitTracker.areas.activities.models.view.ActivityStaticticsViewModel;
import com.elena.habitTracker.areas.habits.entities.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findAllByUserOrderByDateDesc(User user);

    List<Activity> findAllByUserAndHabitOrderByDateAsc(User user, Habit habit);

    @Query("SELECT new com.elena.habitTracker.areas.activities.models.view.ActivityStaticticsViewModel(a.date, count(a)) " +
            "FROM Activity a " +
            "WHERE a.user = ?1 " +
            "AND a.habit = ?2 " +
            "GROUP BY a.date ")
    List<ActivityStaticticsViewModel> findActivitiesStatistics(User u, Habit h);

    @Query("SELECT count(a) " +
            "FROM Activity a " +
            "WHERE a.user = ?1 " +
            "AND a.habit = ?2 " +
            "AND a.date = ?3 " +
            "GROUP BY a.date ")
    Long findActivitiesCountForDate(User u, Habit h, LocalDate d);
}