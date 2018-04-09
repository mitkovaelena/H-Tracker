package org.softuni.habitTracker.areas.habits.services;

import org.softuni.habitTracker.areas.habits.entities.Habit;
import org.softuni.habitTracker.areas.habits.models.binding.HabitAddBindingModel;
import org.softuni.habitTracker.areas.habits.models.binding.HabitEditBindingModel;
import org.softuni.habitTracker.areas.habits.models.view.HabitViewModel;
import org.softuni.habitTracker.areas.users.entities.User;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

public interface HabitService {
    void saveHabit(HabitAddBindingModel habitAddBindingModel);

    HabitEditBindingModel getHabitEditDTOById(Long id);

    HabitViewModel getHabitViewDTOById(Long id);

    Habit getHabitById(Long id);

    void editHabit(Long id, HabitEditBindingModel habitEditBindingModel);

    LocalDate getStartDateById(Long id);

    void deleteHabit(Long id);

    List<HabitViewModel> getAllHabitsByUser(User user);

    List<HabitViewModel> getAllHabitsByUserDueToday(User user) throws ParseException;

    void resetStreak(Long id);

    void calculateNextDueDate(Long id);

    String extractLineChartData(Long id);

    String extractHeatmapData(Habit habit);

    void renewHabit(Long id);
}
