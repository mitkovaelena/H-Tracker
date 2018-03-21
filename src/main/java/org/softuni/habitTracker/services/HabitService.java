package org.softuni.habitTracker.services;

import org.softuni.habitTracker.domain.entities.User;
import org.softuni.habitTracker.domain.models.binding.HabitAddDTO;
import org.softuni.habitTracker.domain.models.binding.HabitEditViewDTO;
import org.softuni.habitTracker.domain.models.binding.HabitViewDTO;

import java.util.Date;
import java.util.List;

public interface HabitService {
    void saveHabit(HabitAddDTO habitAddDTO);

    HabitEditViewDTO getHabitById(Long id);

    void editHabit(Long id, HabitEditViewDTO habitEditViewDTO);

    Date getStartDateById(Long id);

    void deleteHabit(Long id);

    List<HabitViewDTO> findAllHabits(User user);
}
