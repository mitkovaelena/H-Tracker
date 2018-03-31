package org.softuni.habitTracker.services;

import org.modelmapper.ModelMapper;
import org.softuni.habitTracker.domain.entities.Habit;
import org.softuni.habitTracker.domain.entities.Activity;
import org.softuni.habitTracker.domain.entities.User;
import org.softuni.habitTracker.domain.models.binding.HabitAddDTO;
import org.softuni.habitTracker.domain.models.binding.HabitEditDTO;
import org.softuni.habitTracker.domain.models.view.HabitViewDTO;
import org.softuni.habitTracker.repositories.HabitRepository;
import org.softuni.habitTracker.repositories.ActivityRepository;
import org.softuni.habitTracker.repositories.UserRepository;
import org.softuni.habitTracker.util.enums.FrequencyEnum;
import org.softuni.habitTracker.util.enums.PriorityEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HabitServiceImpl implements HabitService {
    private final HabitRepository habitRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public HabitServiceImpl(HabitRepository habitRepository, UserRepository userRepository, ActivityRepository activityRepository, ModelMapper modelMapper) {
        this.habitRepository = habitRepository;
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Habit getHabitById(Long id) {
        return this.habitRepository.findById(id).get();
    }

    @Override
    public void saveHabit(HabitAddDTO habitAddDTO) {
        Habit habit = modelMapper.map(habitAddDTO, Habit.class);
        habit.setFrequency(FrequencyEnum.valueOf(habitAddDTO.getFrequency().toUpperCase().replace(' ', '_')));
        habit.setPriority(PriorityEnum.valueOf(habitAddDTO.getPriority().toUpperCase().replace(' ', '_')));
        habit.setNextDueDate(habit.getStartDate());
        this.habitRepository.save(habit);
    }

    @Override
    public HabitViewDTO getHabitViewDTOById(Long id) {
        Optional<Habit> habitOptional = this.habitRepository.findById(id);
        HabitViewDTO habitViewDTO = null;
        if (habitOptional.isPresent()) {
            habitViewDTO = modelMapper.map(habitOptional.get(), HabitViewDTO.class);
        }

        return habitViewDTO;
    }

    @Override
    public HabitEditDTO getHabitEditDTOById(Long id) {
        Optional<Habit> habitOptional = this.habitRepository.findById(id);
        HabitEditDTO habitEditDTO = null;
        if (habitOptional.isPresent()) {
            habitEditDTO = modelMapper.map(habitOptional.get(), HabitEditDTO.class);
        }
        return habitEditDTO;
    }

    @Override
    public void editHabit(Long id, HabitEditDTO habitEditDTO) {
        Habit habit = this.habitRepository.findById(id).get();
        habit.setTitle(habitEditDTO.getTitle());
        habit.setEndDate(habitEditDTO.getEndDate());
        habit.setFrequency(FrequencyEnum.valueOf(habitEditDTO.getFrequency().toUpperCase().replace(' ', '_')));
        habit.setPriority(PriorityEnum.valueOf(habitEditDTO.getPriority().toUpperCase().replace(' ', '_')));
        this.habitRepository.save(habit);
    }

    @Override
    public LocalDate getStartDateById(Long id) {
        return this.habitRepository.findById(id).get().getStartDate();
    }

    @Override
    public void deleteHabit(Long id) {
        Habit habit = this.habitRepository.findById(id).get();
        for (Activity activity : habit.getActivities()) {
            this.activityRepository.deleteById(activity.getId());
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

    @Override
    public List<HabitViewDTO> findAllHabitsDueToday(User user) throws ParseException {
        List<Habit> habits = this.habitRepository.findAllByUserAndNextDueDate(user, LocalDate.now());
        List<HabitViewDTO> habitViewDTOs = new ArrayList<>();

        for (Habit habit : habits) {
            habitViewDTOs.add(modelMapper.map(habit, HabitViewDTO.class));
        }

        return habitViewDTOs;
    }
}
