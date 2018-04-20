package com.elena.habitTracker.util;

import com.elena.habitTracker.areas.activities.entities.Activity;
import com.elena.habitTracker.areas.activities.models.binding.ActivityAddBindingModel;
import com.elena.habitTracker.areas.activities.models.view.ActivityViewModel;
import com.elena.habitTracker.areas.habits.entities.Habit;
import com.elena.habitTracker.areas.habits.enums.FrequencyEnum;
import com.elena.habitTracker.areas.habits.enums.PriorityEnum;
import com.elena.habitTracker.areas.logs.entities.ApplicationLog;
import com.elena.habitTracker.areas.logs.models.view.ApplicationLogViewModel;
import com.elena.habitTracker.areas.users.entities.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TestsUtils {
    public static User createUserEli() {
        User eli = new User();
        eli.setUsername("eli123");
        eli.setPassword("123456");
        eli.setEmail("eli123@gmail.com");

        return eli;
    }

    public static User createUserVili() {
        User vili =new User();
        vili.setUsername("vili321");
        vili.setPassword("654321");
        vili.setEmail("vili321@gmail.com");

        return vili;
    }

    public static Habit createHabitFitness(User user) {
        Habit fitness = new Habit();
        fitness.setId(1L);
        fitness.setTitle("fitness");
        fitness.setFrequency(FrequencyEnum.DAILY);
        fitness.setPriority(PriorityEnum.LOW);
        fitness.setStartDate(LocalDate.now());
        fitness.setUser(user);

        return fitness;
    }

    public static Habit createHabitCleanEating(User user){
        Habit cleanEating = new Habit();
        cleanEating.setId(2L);
        cleanEating.setTitle("clean eating");
        cleanEating.setFrequency(FrequencyEnum.DAILY);
        cleanEating.setPriority(PriorityEnum.LOW);
        cleanEating.setStartDate(LocalDate.now());
        cleanEating.setUser(user);

        return cleanEating;
    }

    public static Activity createActivity(User user, Habit habit, LocalDate date) {
        Activity activity = new Activity();
        activity.setDate(date);
        activity.setUser(user);
        activity.setHabit(habit);

        return activity;
    }

    public static ActivityAddBindingModel createActivityAddBindingModel(User user, Habit habit, LocalDate date){
        ActivityAddBindingModel activityModel = new ActivityAddBindingModel();
        activityModel.setUser(user);
        activityModel.setHabit(habit);
        activityModel.setDate(date);

        return activityModel;
    }

    public static ActivityViewModel createActivityViewModel(Habit habit, LocalDate date){
        ActivityViewModel activityModel = new ActivityViewModel();
        activityModel.setHabit(habit);
        activityModel.setDate(date);

        return activityModel;
    }

    public static ApplicationLogViewModel createApplicationLogViewModel(User user, LocalDateTime localDateTime){
        ApplicationLogViewModel applicationLogViewModel = new ApplicationLogViewModel();
        applicationLogViewModel.setUser(user.toString());
        applicationLogViewModel.setMessage("TestMessage");
        applicationLogViewModel.setTime(localDateTime);

        return applicationLogViewModel;
    }

    public static ApplicationLog createApplicationLog(User user, LocalDateTime localDateTime){
        ApplicationLog applicationLog = new ApplicationLog();
        applicationLog.setUser(user);
        applicationLog.setMessage("TestMessage");
        applicationLog.setTime(localDateTime);

        return applicationLog;
    }
}
