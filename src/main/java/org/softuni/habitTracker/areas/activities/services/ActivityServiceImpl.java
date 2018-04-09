package org.softuni.habitTracker.areas.activities.services;

import org.modelmapper.ModelMapper;
import org.softuni.habitTracker.areas.activities.entities.Activity;
import org.softuni.habitTracker.areas.activities.models.binding.ActivityAddBindingModel;
import org.softuni.habitTracker.areas.activities.models.view.ActivityViewModel;
import org.softuni.habitTracker.areas.activities.repositories.ActivityRepository;
import org.softuni.habitTracker.areas.habits.entities.Habit;
import org.softuni.habitTracker.areas.habits.repositories.HabitRepository;
import org.softuni.habitTracker.areas.users.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<ActivityViewModel> getAllActivitiesOrderedByDateDesc(User user) {
        List<Activity> activities = this.activityRepository.findAllByUserOrderByDateDesc(user);
        List<ActivityViewModel> activityViewModels = new ArrayList<>();

        for (Activity activity : activities) {
            activityViewModels.add(modelMapper.map(activity, ActivityViewModel.class));
        }

        return activityViewModels;
    }

    @Override
    public void saveActivity(ActivityAddBindingModel activityAddBindingModel) {
        Activity activity = modelMapper.map(activityAddBindingModel, Activity.class);
        Habit habit = activityAddBindingModel.getHabit();

        LocalDate nextDueDate = habit.calculateNextDueDate();

        if(habit.getNextDueDate().equals(activity.getDate())) {
            habit.setStreak(habit.getStreak() + 1);
        }

        if (habit.getEndDate() == null || !nextDueDate.isAfter(habit.getEndDate())) {
            habit.setNextDueDate(nextDueDate);
        } else {
            habit.setNextDueDate(null);
            activityAddBindingModel.setHabit(habit);
        }

        this.habitRepository.save(habit);
        this.activityRepository.save(activity);
    }
}
