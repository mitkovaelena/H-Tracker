package com.elena.habitTracker.areas.habits.services;

import com.elena.habitTracker.areas.habits.models.binding.HabitAddBindingModel;
import com.elena.habitTracker.areas.habits.models.binding.HabitEditBindingModel;
import com.elena.habitTracker.areas.habits.models.view.HabitViewModel;
import com.elena.habitTracker.areas.habits.models.view.HabitsPageViewModel;
import com.elena.habitTracker.util.ApplicationConstants;
import com.elena.habitTracker.areas.users.entities.User;
import com.elena.habitTracker.areas.habits.entities.Habit;
import org.springframework.data.domain.Pageable;

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

    HabitsPageViewModel getAllHabitsByUser(User user, Pageable pageable);

    List<HabitViewModel> getAllHabitsByUser(User user);

    List<HabitViewModel> getAllHabitsByUserDueToday(User user) throws ParseException;

    void resetStreak(Long id);

    void calculateNextDueDate(Long id);

    String extractLineChartData(Long id);

    String extractHeatmapData(Long id);

    void renewHabit(Long id);

    default long getTotalPages() {
        return getTotalPages(ApplicationConstants.DEFAULT_VIEWS_COUNT_PER_PAGE);
    }

    long getTotalPages(int size);

}
