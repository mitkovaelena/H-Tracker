package com.elena.habitTracker.util;

import com.elena.habitTracker.areas.activities.entities.Activity;
import com.elena.habitTracker.areas.activities.models.binding.ActivityAddBindingModel;
import com.elena.habitTracker.areas.activities.models.view.ActivityViewModel;
import com.elena.habitTracker.areas.habits.entities.Habit;
import com.elena.habitTracker.areas.habits.enums.FrequencyEnum;
import com.elena.habitTracker.areas.habits.enums.PriorityEnum;
import com.elena.habitTracker.areas.habits.models.binding.HabitAddBindingModel;
import com.elena.habitTracker.areas.logs.entities.ApplicationLog;
import com.elena.habitTracker.areas.logs.models.view.ApplicationLogViewModel;
import com.elena.habitTracker.areas.roles.entities.Role;
import com.elena.habitTracker.areas.roles.enums.RoleEnum;
import com.elena.habitTracker.areas.users.entities.User;
import com.elena.habitTracker.areas.users.models.binding.UserEditBindingModel;
import com.elena.habitTracker.areas.users.models.binding.UserRegisterBindingModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestsUtils {
    public static User createUserEli() {
        User eli = new User();
        eli.setUsername("eli123");
        eli.setPassword("123456");
        eli.setEmail("eli123@gmail.com");
        eli.setFirstName("Elena");
        eli.setLastName("Nikolova");

        return eli;
    }

    public static UserRegisterBindingModel createUserRegisterBindingModelEli() {
        UserRegisterBindingModel eli = new UserRegisterBindingModel();
        eli.setUsername("eli123");
        eli.setPassword("123456");
        eli.setEmail("eli123@gmail.com");
        eli.setFirstName("Elena");
        eli.setLastName("Nikolova");

        return eli;
    }

    public static UserEditBindingModel createUserEditBindingModelEli() {
        UserEditBindingModel eli = new UserEditBindingModel();
        eli.setEmail("eli123@gmail.com");
        eli.setFirstName("Elena");
        eli.setLastName("Nikolova");

        return eli;
    }

    public static User createUserVili() {
        User vili =new User();
        vili.setUsername("vili321");
        vili.setPassword("654321");
        vili.setEmail("vili321@gmail.com");
        vili.setFirstName("Violeta");
        vili.setLastName("Nikolova");

        return vili;
    }

    public static Habit createHabitFitness(User user) {
        Habit fitness = new Habit();
        fitness.setTitle("fitness");
        fitness.setFrequency(FrequencyEnum.DAILY);
        fitness.setPriority(PriorityEnum.LOW);
        fitness.setStartDate(LocalDate.now());
        fitness.setUser(user);

        return fitness;
    }

    public static HabitAddBindingModel createHabitAddModelFitness(User user) {
        HabitAddBindingModel fitness = new HabitAddBindingModel();
        fitness.setTitle("fitness");
        fitness.setFrequency(FrequencyEnum.DAILY.getFrequencyName());
        fitness.setPriority(PriorityEnum.LOW.getPriorityName());
        fitness.setStartDate(LocalDate.now());
        fitness.setUser(user);

        return fitness;
    }

    public static Habit createHabitCleanEating(User user){
        Habit cleanEating = new Habit();
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

    public static ApplicationLog createApplicationLog(User user, LocalDateTime localDateTime){
        ApplicationLog applicationLog = new ApplicationLog();
        applicationLog.setUser(user);
        applicationLog.setMessage("Test Message");
        applicationLog.setTime(localDateTime);

        return applicationLog;
    }
}
