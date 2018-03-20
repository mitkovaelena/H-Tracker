package org.softuni.habitTracker.services;

import org.modelmapper.ModelMapper;
import org.softuni.habitTracker.domain.entities.Habit;
import org.softuni.habitTracker.domain.entities.User;
import org.softuni.habitTracker.domain.models.binding.HabitAddDTO;
import org.softuni.habitTracker.repositories.HabitRepository;
import org.softuni.habitTracker.util.enums.HabitFrequencyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class HabitServiceImpl implements HabitService {
    private final HabitRepository habitRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public HabitServiceImpl(HabitRepository habitRepository, ModelMapper modelMapper) {
        this.habitRepository = habitRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean saveHabit(HabitAddDTO habitAddDTO, User user) {
        Habit habit = modelMapper.map(habitAddDTO, Habit.class);
        habit.setUser(user);
        habit.setFrequency(HabitFrequencyEnum.valueOf(habitAddDTO.getFrequency().toUpperCase().replace(' ', '_')));
        this.habitRepository.save(habit);
        return true;
    }
}
