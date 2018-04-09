package org.softuni.habitTracker.areas.habits.services;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.softuni.habitTracker.areas.activities.entities.Activity;
import org.softuni.habitTracker.areas.activities.models.view.ActivityStaticticsViewModel;
import org.softuni.habitTracker.areas.activities.repositories.ActivityRepository;
import org.softuni.habitTracker.areas.habits.entities.Habit;
import org.softuni.habitTracker.areas.habits.enums.FrequencyEnum;
import org.softuni.habitTracker.areas.habits.enums.PriorityEnum;
import org.softuni.habitTracker.areas.habits.models.binding.HabitAddBindingModel;
import org.softuni.habitTracker.areas.habits.models.binding.HabitEditBindingModel;
import org.softuni.habitTracker.areas.habits.models.json.DataSetJsonObject;
import org.softuni.habitTracker.areas.habits.models.json.LineChartJsonObject;
import org.softuni.habitTracker.areas.habits.models.view.HabitViewModel;
import org.softuni.habitTracker.areas.habits.repositories.HabitRepository;
import org.softuni.habitTracker.areas.users.entities.User;
import org.softuni.habitTracker.areas.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

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
    public void saveHabit(HabitAddBindingModel habitAddBindingModel) {
        Habit habit = modelMapper.map(habitAddBindingModel, Habit.class);
        habit.setFrequency(FrequencyEnum.valueOf(habitAddBindingModel.getFrequency().toUpperCase().replace(' ', '_')));
        habit.setPriority(PriorityEnum.valueOf(habitAddBindingModel.getPriority().toUpperCase().replace(' ', '_')));
        habit.setNextDueDate(habit.getStartDate());
        this.habitRepository.save(habit);
    }

    @Override
    public HabitViewModel getHabitViewDTOById(Long id) {
        Optional<Habit> habitOptional = this.habitRepository.findById(id);
        HabitViewModel habitViewModel = null;
        if (habitOptional.isPresent()) {
            habitViewModel = modelMapper.map(habitOptional.get(), HabitViewModel.class);
        }

        return habitViewModel;
    }

    @Override
    public HabitEditBindingModel getHabitEditDTOById(Long id) {
        Optional<Habit> habitOptional = this.habitRepository.findById(id);
        HabitEditBindingModel habitEditBindingModel = null;
        if (habitOptional.isPresent()) {
            habitEditBindingModel = modelMapper.map(habitOptional.get(), HabitEditBindingModel.class);
        }
        return habitEditBindingModel;
    }

    @Override
    public void editHabit(Long id, HabitEditBindingModel habitEditBindingModel) {
        Habit habit = this.habitRepository.findById(id).get();
        habit.setTitle(habitEditBindingModel.getTitle());
        habit.setEndDate(habitEditBindingModel.getEndDate());
        habit.setFrequency(FrequencyEnum.valueOf(habitEditBindingModel.getFrequency().toUpperCase().replace(' ', '_')));
        habit.setPriority(PriorityEnum.valueOf(habitEditBindingModel.getPriority().toUpperCase().replace(' ', '_')));
        habit.setNextDueDate(habit.calculateNextDueDate());
        this.habitRepository.save(habit);
    }

    @Override
    public LocalDate getStartDateById(Long id) {
        return this.habitRepository.findById(id).get().getStartDate();
    }

    @Override
    public void resetStreak(Long id) {
        Habit habit = this.habitRepository.findById(id).get();
        habit.setStreak(0);
        this.habitRepository.save(habit);
    }

    @Override
    public void calculateNextDueDate(Long id) {
        Habit habit = this.habitRepository.findById(id).get();
        habit.setNextDueDate(habit.calculateNextDueDate());
        this.habitRepository.save(habit);
    }

    @Override
    public String extractLineChartData(Long id) {
        Habit habit = this.habitRepository.findById(id).get();
        List<ActivityStaticticsViewModel> activities = this.getLastActivities(habit, 10);
        DataSetJsonObject dataset = new DataSetJsonObject(activities.stream().map(ActivityStaticticsViewModel::getCount).collect(Collectors.toList()));

        LineChartJsonObject lcjo = new LineChartJsonObject(
                activities.stream().map(x -> x.getDate()).collect(Collectors.toList()), new DataSetJsonObject[]{dataset});

        Gson gson = new Gson();
        return gson.toJson(lcjo);
    }

    @Override
    public String extractHeatmapData(Habit habit) {
        List<ActivityStaticticsViewModel> activities = this.activityRepository.findActivitiesStatistics(habit.getUser(), habit);
        ZoneId zoneId = ZoneId.systemDefault();

        HashMap<String, Long> timestamps = new HashMap<>();
        activities.forEach(a -> timestamps.put(String.valueOf(a.getDate().atStartOfDay(zoneId).toEpochSecond()), (a.getCount())));

        Gson gson = new Gson();
        return gson.toJson(timestamps);
    }

    @Override
    public void renewHabit(Long id) {
        Habit habit = this.habitRepository.findById(id).get();
        habit.setNextDueDate(LocalDate.now());
        this.habitRepository.save(habit);
    }

    private List<ActivityStaticticsViewModel> getLastActivities(Habit habit, int days) {
        LinkedList<ActivityStaticticsViewModel> lastActivities = new LinkedList<>();
        LocalDate maxDate = LocalDate.now();

        while (lastActivities.size() < days) {
            Long count = this.activityRepository.findActivitiesCountForDate(habit.getUser(), habit, maxDate);
            lastActivities.addFirst(new ActivityStaticticsViewModel(maxDate, count == null ? 0L : count));
            maxDate = maxDate.minusDays(1);
        }

        return lastActivities;
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
    public List<HabitViewModel> getAllHabitsByUser(User user) {
        List<Habit> habits = this.habitRepository.findAllByUser(user);
        List<HabitViewModel> habitViewModels = new ArrayList<>();

        for (Habit habit : habits) {
            habitViewModels.add(modelMapper.map(habit, HabitViewModel.class));
        }

        return habitViewModels;
    }

    @Override
    public List<HabitViewModel> getAllHabitsByUserDueToday(User user) throws ParseException {
        List<Habit> habits = this.habitRepository.findAllByUserAndNextDueDate(user, LocalDate.now());
        List<HabitViewModel> habitViewModels = new ArrayList<>();

        for (Habit habit : habits) {
            habitViewModels.add(modelMapper.map(habit, HabitViewModel.class));
        }

        return habitViewModels;
    }
}
