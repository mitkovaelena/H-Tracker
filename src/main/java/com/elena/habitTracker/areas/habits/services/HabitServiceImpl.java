package com.elena.habitTracker.areas.habits.services;

import com.elena.habitTracker.areas.activities.entities.Activity;
import com.elena.habitTracker.areas.activities.models.view.ActivityStaticticsViewModel;
import com.elena.habitTracker.areas.activities.repositories.ActivityRepository;
import com.elena.habitTracker.areas.habits.entities.Habit;
import com.elena.habitTracker.areas.habits.enums.FrequencyEnum;
import com.elena.habitTracker.areas.habits.enums.PriorityEnum;
import com.elena.habitTracker.areas.habits.models.binding.HabitAddBindingModel;
import com.elena.habitTracker.areas.habits.models.binding.HabitEditBindingModel;
import com.elena.habitTracker.areas.habits.models.json.DataSetJsonObject;
import com.elena.habitTracker.areas.habits.models.json.LineChartJsonObject;
import com.elena.habitTracker.areas.habits.models.view.HabitViewModel;
import com.elena.habitTracker.areas.habits.models.view.HabitsPageViewModel;
import com.elena.habitTracker.areas.habits.repositories.HabitRepository;
import com.elena.habitTracker.areas.users.entities.User;
import com.elena.habitTracker.areas.users.repositories.UserRepository;
import com.elena.habitTracker.errors.ResourceNotFoundException;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
        Optional<Habit> habitOptional = this.habitRepository.findById(id);
        if (!habitOptional.isPresent()) {
            throw new ResourceNotFoundException();
        }

        return habitOptional.get();
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
        Habit habit = this.getHabitById(id);
        HabitViewModel habitViewModel = modelMapper.map(habit, HabitViewModel.class);
        habitViewModel.setUserId(habit.getUser().getId());

        return habitViewModel;
    }

    @Override
    public HabitEditBindingModel getHabitEditDTOById(Long id) {
        return modelMapper.map(this.getHabitById(id), HabitEditBindingModel.class);
    }

    @Override
    public void editHabit(Long id, HabitEditBindingModel habitEditBindingModel) {
        Habit habit = this.getHabitById(id);
        habit.setTitle(habitEditBindingModel.getTitle());
        habit.setEndDate(habitEditBindingModel.getEndDate());
        habit.setFrequency(FrequencyEnum.valueOf(habitEditBindingModel.getFrequency().toUpperCase().replace(' ', '_')));
        habit.setPriority(PriorityEnum.valueOf(habitEditBindingModel.getPriority().toUpperCase().replace(' ', '_')));
        habit.setNextDueDate(habit.getStartDate());
        habit.setNextDueDate(habit.calculateNextDueDate());

        this.habitRepository.save(habit);
    }

    @Override
    public LocalDate getStartDateById(Long id) {
        return this.getHabitById(id).getStartDate();
    }

    @Override
    public void resetStreak(Long id) {
        Habit habit = this.getHabitById(id);
        habit.setStreak(0);

        this.habitRepository.save(habit);
    }

    @Override
    public void calculateNextDueDate(Long id) {
        Habit habit = this.getHabitById(id);
        habit.setNextDueDate(habit.calculateNextDueDate());

        this.habitRepository.save(habit);
    }

    @Override
    public String extractLineChartData(Long id) {
        Habit habit = this.getHabitById(id);

        List<ActivityStaticticsViewModel> activities = this.getLastActivities(habit, 10);
        DataSetJsonObject dataset = new DataSetJsonObject(activities.stream()
                .map(ActivityStaticticsViewModel::getCount).collect(Collectors.toList()));

        LineChartJsonObject lcjo = new LineChartJsonObject(
                activities.stream().map(ActivityStaticticsViewModel::getDate)
                        .collect(Collectors.toList()), new DataSetJsonObject[]{dataset});

        return new Gson().toJson(lcjo);
    }

    @Override
    public String extractHeatmapData(Long id) {
        Habit habit = this.getHabitById(id);
        List<ActivityStaticticsViewModel> activities = this.activityRepository.findActivitiesStatistics(habit.getUser(), habit);
        ZoneId zoneId = ZoneId.systemDefault();

        HashMap<String, Long> timestamps = new HashMap<>();
        activities.forEach(a -> timestamps.put(String.valueOf(a.getDate().atStartOfDay(zoneId).toEpochSecond()), (a.getCount())));

        return new Gson().toJson(timestamps);
    }

    @Override
    public void renewHabit(Long id) {
        Habit habit = this.getHabitById(id);
        habit.setNextDueDate(LocalDate.now());
        habit.setEndDate(null);
        habit.setStreak(0);

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
        Habit habit = this.getHabitById(id);
        for (Activity activity : habit.getActivities()) {
            this.activityRepository.deleteById(activity.getId());
        }
        User user = habit.getUser();
        user.getHabits().remove(habit);
        this.userRepository.save(user); //ToDO test if needed

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

    public HabitsPageViewModel getHabitsPageByUser(User user, Pageable pageable) {
        Page<Habit> habitsPage = this.habitRepository.findAllByUser(user, pageable);
        int totalElements = (int) habitsPage.getTotalElements();

        Page<HabitViewModel> habitViewModelsPage = new PageImpl<>(
                habitsPage.stream()
                        .map(log -> this.modelMapper.map(log, HabitViewModel.class))
                        .collect(Collectors.toList()), pageable, totalElements);

        HabitsPageViewModel habitsPageViewModel = new HabitsPageViewModel();
        habitsPageViewModel.setHabits(habitViewModelsPage);
        habitsPageViewModel.setTotalPagesCount(habitViewModelsPage.getTotalPages());

        return habitsPageViewModel;
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
