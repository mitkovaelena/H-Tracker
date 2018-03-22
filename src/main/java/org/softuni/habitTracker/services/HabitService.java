package org.softuni.habitTracker.services;

import org.softuni.habitTracker.domain.entities.User;
import org.softuni.habitTracker.domain.models.binding.HabitAddDTO;
import org.softuni.habitTracker.domain.models.binding.HabitEditDTO;
import org.softuni.habitTracker.domain.models.binding.HabitViewDTO;

import java.util.Date;
import java.util.List;

public interface HabitService {
    void saveHabit(HabitAddDTO habitAddDTO);

    HabitEditDTO getHabitById(Long id);

    void editHabit(Long id, HabitEditDTO habitEditDTO);

    Date getStartDateById(Long id);

    void deleteHabit(Long id);

    List<HabitViewDTO> findAllHabits(User user);
}
