package org.softuni.habitTracker.services;

import org.modelmapper.ModelMapper;
import org.softuni.habitTracker.domain.entities.Habit;
import org.softuni.habitTracker.domain.entities.Log;
import org.softuni.habitTracker.domain.entities.User;
import org.softuni.habitTracker.domain.models.binding.HabitAddDTO;
import org.softuni.habitTracker.domain.models.binding.HabitEditDTO;
import org.softuni.habitTracker.domain.models.binding.HabitViewDTO;
import org.softuni.habitTracker.repositories.HabitRepository;
import org.softuni.habitTracker.repositories.LogRepository;
import org.softuni.habitTracker.repositories.UserRepository;
import org.softuni.habitTracker.util.enums.HabitFrequencyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HabitServiceImpl implements HabitService {
    private final HabitRepository habitRepository;
    private final UserRepository userRepository;
    private final LogRepository logRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public HabitServiceImpl(HabitRepository habitRepository, UserRepository userRepository, LogRepository logRepository, ModelMapper modelMapper) {
        this.habitRepository = habitRepository;
        this.userRepository = userRepository;
        this.logRepository = logRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void saveHabit(HabitAddDTO habitAddDTO) {
        Habit habit = modelMapper.map(habitAddDTO, Habit.class);
        habit.setFrequency(HabitFrequencyEnum.valueOf(habitAddDTO.getFrequency().toUpperCase().replace(' ', '_')));
        this.habitRepository.save(habit);
    }

    @Override
    public HabitEditDTO getHabitById(Long id) {
        Optional<Habit> habitOptional = this.habitRepository.findById(id);
        HabitEditDTO habitEditDTO = null;
        if (habitOptional.isPresent()) {
            habitEditDTO = modelMapper.map(habitOptional.get(), HabitEditDTO.class);
        }
        return habitEditDTO;
    }

    @Override
    public void editHabit(Long id, HabitEditDTO habitEditDTO) {
        Habit editedHabit = modelMapper.map(habitEditDTO, Habit.class);
        Habit habit = this.habitRepository.findById(id).get();
        editedHabit.setUser(habit.getUser());
        editedHabit.setId(id);
        editedHabit.setLogs(habit.getLogs());
        editedHabit.setStartDate(habit.getStartDate());
        editedHabit.setFrequency(HabitFrequencyEnum.valueOf(habitEditDTO.getFrequency().toUpperCase().replace(' ', '_')));
        this.habitRepository.save(editedHabit);
    }

    @Override
    public Date getStartDateById(Long id) {
        return this.habitRepository.findById(id).get().getStartDate();
    }

    @Override
    public void deleteHabit(Long id) {
        Habit habit = this.habitRepository.findById(id).get();
        for (Log log : habit.getLogs()) {
            this.logRepository.deleteById(log.getId());
        }
        User user = habit.getUser();
        user.getHabits().remove(habit);
        this.userRepository.save(user);

        this.habitRepository.deleteById(id);
    }

    @Override
    public List<HabitViewDTO> findAllHabits(User user) {
        List<Habit> habits = this.habitRepository.findAllByUser(user);
        List<HabitViewDTO> habitViewDTOs = new ArrayList<>();

        for (Habit habit : habits) {
            habitViewDTOs.add(modelMapper.map(habit, HabitViewDTO.class));
        }

        return habitViewDTOs;
    }
}
