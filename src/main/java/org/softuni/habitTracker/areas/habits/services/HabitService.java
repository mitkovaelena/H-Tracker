package org.softuni.habitTracker.areas.habits.services;

import org.softuni.habitTracker.areas.habits.entities.Habit;
import org.softuni.habitTracker.areas.habits.models.binding.HabitAddDTO;
import org.softuni.habitTracker.areas.habits.models.binding.HabitEditDTO;
import org.softuni.habitTracker.areas.habits.models.view.HabitViewDTO;
import org.softuni.habitTracker.areas.users.entities.User;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

public interface HabitService {
    void saveHabit(HabitAddDTO habitAddDTO);

    HabitEditDTO getHabitEditDTOById(Long id);

    HabitViewDTO getHabitViewDTOById(Long id);

    Habit getHabitById(Long id);

    void editHabit(Long id, HabitEditDTO habitEditDTO);

    LocalDate getStartDateById(Long id);

    void deleteHabit(Long id);

    List<HabitViewDTO> getAllHabitsByUser(User user);

    List<HabitViewDTO> getAllHabitsByUserDueToday(User user) throws ParseException;

    void resetStreak(Long id);

    void calculateNextDueDate(Long id);
}
