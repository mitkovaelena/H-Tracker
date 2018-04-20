package com.elena.habitTracker.tasks;

import com.elena.habitTracker.areas.habits.entities.Habit;
import com.elena.habitTracker.areas.habits.services.HabitService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ScheduledTasks {
    private HabitService habitService;

    public ScheduledTasks(HabitService habitService) {
        this.habitService = habitService;
    }

    @Scheduled(cron = " 50 59 23 * * *")
    public void calculateStreak(){
        List<Habit> habits = this.habitService.getAllHabits();

        for (Habit habit : habits) {
            if (habit.getNextDueDate() != null && !habit.getNextDueDate().isAfter(LocalDate.now())) {
                this.habitService.resetStreak(habit.getId());
                this.habitService.calculateNextDueDate(habit.getId());
            }
        }
    }
}
