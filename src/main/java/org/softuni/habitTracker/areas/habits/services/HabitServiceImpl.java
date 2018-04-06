package org.softuni.habitTracker.areas.habits.services;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.softuni.habitTracker.areas.activities.entities.Activity;
import org.softuni.habitTracker.areas.activities.models.view.ActivityStatictics;
import org.softuni.habitTracker.areas.activities.repositories.ActivityRepository;
import org.softuni.habitTracker.areas.habits.entities.Habit;
import org.softuni.habitTracker.areas.habits.enums.FrequencyEnum;
import org.softuni.habitTracker.areas.habits.enums.PriorityEnum;
import org.softuni.habitTracker.areas.habits.models.binding.HabitAddDTO;
import org.softuni.habitTracker.areas.habits.models.binding.HabitEditDTO;
import org.softuni.habitTracker.areas.habits.models.json.DataSet;
import org.softuni.habitTracker.areas.habits.models.json.LineChartJsonObject;
import org.softuni.habitTracker.areas.habits.models.view.HabitViewDTO;
import org.softuni.habitTracker.areas.habits.repositories.HabitRepository;
import org.softuni.habitTracker.areas.users.entities.User;
import org.softuni.habitTracker.areas.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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
        List<ActivityStatictics> activities = this.getLastActivities(habit);
        DataSet dataset = new DataSet(activities.stream().map(ActivityStatictics::getCount).collect(Collectors.toList()));

        LineChartJsonObject lcjo = new LineChartJsonObject(
                activities.stream().map(x -> x.getDate()).collect(Collectors.toList()), new DataSet[]{dataset});

        Gson gson = new Gson();
        return gson.toJson(lcjo);
    }

    private List<ActivityStatictics> getLastActivities(Habit habit) {
        LinkedList<ActivityStatictics> lastActivities = new LinkedList<>();
        LocalDate maxDate = LocalDate.now();

        while (lastActivities.size() < 10) {
            Long count = this.activityRepository.findActivitiesCountForDate(habit.getUser(), habit, maxDate);
            lastActivities.addFirst(new ActivityStatictics(maxDate, count == null ? 0L : count));
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
    public List<HabitViewDTO> getAllHabitsByUser(User user) {
        List<Habit> habits = this.habitRepository.findAllByUser(user);
        List<HabitViewDTO> habitViewDTOs = new ArrayList<>();

        for (Habit habit : habits) {
            habitViewDTOs.add(modelMapper.map(habit, HabitViewDTO.class));
        }

        return habitViewDTOs;
    }

    @Override
    public List<HabitViewDTO> getAllHabitsByUserDueToday(User user) throws ParseException {
        List<Habit> habits = this.habitRepository.findAllByUserAndNextDueDate(user, LocalDate.now());
        List<HabitViewDTO> habitViewDTOs = new ArrayList<>();

        for (Habit habit : habits) {
            habitViewDTOs.add(modelMapper.map(habit, HabitViewDTO.class));
        }

        return habitViewDTOs;
    }
}
