package com.elena.habitTracker.areas.activities.services;

import com.elena.habitTracker.areas.activities.entities.Activity;
import com.elena.habitTracker.areas.activities.models.binding.ActivityAddBindingModel;
import com.elena.habitTracker.areas.activities.models.view.ActivitiesPageViewModel;
import com.elena.habitTracker.areas.activities.models.view.ActivityViewModel;
import com.elena.habitTracker.areas.activities.repositories.ActivityRepository;
import com.elena.habitTracker.areas.habits.entities.Habit;
import com.elena.habitTracker.areas.habits.repositories.HabitRepository;
import com.elena.habitTracker.areas.users.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Collectors;

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
    public ActivitiesPageViewModel getAllActivitiesOrderedByDateDesc(User user, Pageable pageable) {
        Page<Activity> activitiesPage = this.activityRepository.findAllByUserOrderByDateDesc(user, pageable);
        int totalElements = (int) activitiesPage.getTotalElements();

        Page<ActivityViewModel> applicationLogViewModelPage = new PageImpl<>(
                activitiesPage.stream()
                        .map(log -> this.modelMapper.map(log, ActivityViewModel.class))
                        .collect(Collectors.toList()), pageable, totalElements);

        ActivitiesPageViewModel activitiesPageViewModel = new ActivitiesPageViewModel();
        activitiesPageViewModel.setActivities(applicationLogViewModelPage);
        activitiesPageViewModel.setTotalPagesCount(applicationLogViewModelPage.getTotalPages());

        return activitiesPageViewModel;
    }

    @Override
    public void saveActivity(ActivityAddBindingModel activityAddBindingModel) {
        Activity activity = modelMapper.map(activityAddBindingModel, Activity.class);
        Habit habit = activityAddBindingModel.getHabit();

        habit.setNextDueDate(habit.getStartDate());
        LocalDate nextDueDate = habit.calculateNextDueDate();

        if (habit.getNextDueDate().equals(activity.getDate())) {
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
