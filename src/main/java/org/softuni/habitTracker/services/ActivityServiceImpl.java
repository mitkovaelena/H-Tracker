package org.softuni.habitTracker.services;

import org.modelmapper.ModelMapper;
import org.softuni.habitTracker.domain.entities.Activity;
import org.softuni.habitTracker.domain.entities.Habit;
import org.softuni.habitTracker.domain.entities.User;
import org.softuni.habitTracker.domain.models.binding.ActivityAddDTO;
import org.softuni.habitTracker.domain.models.view.ActivityViewDTO;
import org.softuni.habitTracker.repositories.ActivityRepository;
import org.softuni.habitTracker.repositories.HabitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepository;
    private final HabitRepository habitRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public ActivityServiceImpl(ActivityRepository activityRepository, HabitRepository habitRepository, ModelMapper modelMapper) {
        this.activityRepository = activityRepository;
        this.habitRepository = habitRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ActivityViewDTO> findAllActivitiesOrderedByDateDesc(User user) {
        List<Activity> activities = this.activityRepository.findAllByUserOrderByDateDesc(user);
        List<ActivityViewDTO> activityViewDTOs = new ArrayList<>();

        for (Activity activity : activities) {
            activityViewDTOs.add(modelMapper.map(activity, ActivityViewDTO.class));
        }

        return activityViewDTOs;
    }

    @Override
    public void saveActivity(ActivityAddDTO activityAddDTO) {
        Activity activity = modelMapper.map(activityAddDTO, Activity.class);
        Habit habit = activityAddDTO.getHabit();

        LocalDate nextDueDate = habit.calculateNextDueDate();
        habit.setStreak(habit.getStreak() + 1);
        if(habit.getEndDate() == null || !nextDueDate.isAfter(habit.getEndDate())){
            habit.setNextDueDate(nextDueDate);
        } else {
            habit.setNextDueDate(LocalDate.now().minusDays(1));
            //ToDo: Habit completed!
        }
        this.habitRepository.save(habit);
        this.activityRepository.save(activity);
    }


}
