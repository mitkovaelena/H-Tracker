package com.elena.habitTracker.services;

import com.elena.habitTracker.areas.activities.repositories.ActivityRepository;
import com.elena.habitTracker.areas.habits.entities.Habit;
import com.elena.habitTracker.areas.habits.enums.FrequencyEnum;
import com.elena.habitTracker.areas.habits.enums.PriorityEnum;
import com.elena.habitTracker.areas.habits.models.binding.HabitAddBindingModel;
import com.elena.habitTracker.areas.habits.models.binding.HabitEditBindingModel;
import com.elena.habitTracker.areas.habits.models.view.HabitViewModel;
import com.elena.habitTracker.areas.habits.repositories.HabitRepository;
import com.elena.habitTracker.areas.habits.services.HabitService;
import com.elena.habitTracker.areas.habits.services.HabitServiceImpl;
import com.elena.habitTracker.areas.users.entities.User;
import com.elena.habitTracker.errors.ResourceNotFoundException;
import com.elena.habitTracker.util.TestsUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@EnableSpringDataWebSupport
public class HabitServiceTests {
    @Mock
    private HabitRepository habitRepository;

    @Mock
    private ActivityRepository activityRepository;

    private HabitService habitService;

    private Habit fitness;

    private User eli;

    private PageRequest pageable;

    @Before
    public void setUp() {
        habitService = new HabitServiceImpl(habitRepository, activityRepository, new ModelMapper());

        this.eli = new User("eli123", "123456", "eli123@gmail.com", "Elena", "Nikolova");
        this.fitness = new Habit("fitness", FrequencyEnum.DAILY, PriorityEnum.LOW, LocalDate.now(), eli);

        pageable = PageRequest.of(1, 2);

        when(this.habitRepository.save(any()))
                .thenAnswer(a -> a.getArgument(0));

        when(this.habitRepository.findById(1L))
                .thenAnswer(a -> Optional.ofNullable(fitness));

        when(this.habitRepository.findById(2L))
                .thenAnswer(a -> Optional.ofNullable(null));
    }


    @Test
    public void testGetHabitById_givenValidHabit_shouldReturnHabit() {
        //act
        Habit habit = this.habitService.getHabitById(1L);

        //assert
        Assert.assertNotNull("Habit is null when loaded by id", habit);
        Assert.assertEquals("Wrong title when loaded by username", fitness.getTitle(), habit.getTitle());
        Assert.assertEquals("Wrong start date when loaded by username", fitness.getStartDate(), habit.getStartDate());
        Assert.assertEquals("Wrong frequency when loaded by username", fitness.getFrequency(), habit.getFrequency());
        Assert.assertEquals("Wrong priority when loaded by username", fitness.getPriority(), habit.getPriority());
        Assert.assertEquals("Wrong user when loaded by username", fitness.getUser().toString(), habit.getUser().toString());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetHabitById_givenNotValidHabit_shouldThrowResourceNotFoundException() {
        //act
        this.habitService.getHabitById(2L);
    }

    @Test
    public void testSaveHabit_givenValidHabit_shouldMapFieldsCorrectly() {
        HabitAddBindingModel habitAddBindingModel = new HabitAddBindingModel("fitness", FrequencyEnum.DAILY.getFrequencyName(), PriorityEnum.LOW.getPriorityName(), LocalDate.now(), eli);

        //act
        Habit createdHabit = this.habitService.saveHabit(habitAddBindingModel);

        //assert
        Assert.assertNotNull("Habit is null after creation", createdHabit);

        Assert.assertEquals("Wrong title when loaded by username", habitAddBindingModel.getTitle(), createdHabit.getTitle());
        Assert.assertEquals("Wrong start date when loaded by username", habitAddBindingModel.getStartDate(), createdHabit.getStartDate());
        Assert.assertEquals("Wrong frequency when loaded by username", habitAddBindingModel.getFrequency(), createdHabit.getFrequency().getFrequencyName());
        Assert.assertEquals("Wrong priority when loaded by username", habitAddBindingModel.getPriority(), createdHabit.getPriority().getPriorityName());
        Assert.assertEquals("Wrong user when loaded by username", habitAddBindingModel.getUser().toString(), createdHabit.getUser().toString());

    }

    @Test
    public void testGetHabitViewDTOById_givenValidHabit_shouldMapFieldsCorrectly() {
        //act
        HabitViewModel habitViewModel = this.habitService.getHabitViewDTOById(1L);

        //assert
        Assert.assertNotNull("Habit is null after creation", habitViewModel);

        Assert.assertEquals("Wrong title when loaded by username", fitness.getTitle(), habitViewModel.getTitle());
        Assert.assertEquals("Wrong start date when loaded by username", fitness.getStartDate(), habitViewModel.getStartDate());
        Assert.assertEquals("Wrong frequency when loaded by username", fitness.getFrequency().getFrequencyName(), habitViewModel.getFrequency());
        Assert.assertEquals("Wrong priority when loaded by username", fitness.getPriority().getPriorityName(), habitViewModel.getPriority());
        Assert.assertEquals("Wrong username when loaded by username", fitness.getUser().getUsername(), habitViewModel.getUsername());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetHabitViewDTOById_givenNotValidHabit_shouldThrowResourceNotFoundException() {
        //act
        this.habitService.getHabitViewDTOById(2L);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetHabitEditDTOById_givenNotValidHabit_shouldThrowResourceNotFoundException() {
        //act
        this.habitService.getHabitEditDTOById(2L);
    }

    @Test
    public void testEditHabit_givenValidHabit_shouldMapFieldsCorrectly() {
        HabitEditBindingModel habitEditBindingModel = new HabitEditBindingModel("fitness", FrequencyEnum.DAILY.getFrequencyName(), PriorityEnum.LOW.getPriorityName());
        //act
        Habit editedHabit = this.habitService.editHabit(1L, habitEditBindingModel);

        //assert
        Assert.assertNotNull("Habit is null after creation", editedHabit);

        Assert.assertEquals("Wrong title when loaded by username", habitEditBindingModel.getTitle(), editedHabit.getTitle());
        Assert.assertEquals("Wrong start date when loaded by username", fitness.getStartDate(), editedHabit.getStartDate());
        Assert.assertEquals("Wrong frequency when loaded by username", habitEditBindingModel.getFrequency(), editedHabit.getFrequency().getFrequencyName());
        Assert.assertEquals("Wrong priority when loaded by username", habitEditBindingModel.getPriority(), editedHabit.getPriority().getPriorityName());
        Assert.assertEquals("Wrong user when loaded by username", fitness.getUser().toString(), editedHabit.getUser().toString());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testEditHabit_givenNotValidHabit_shouldThrowResourceNotFoundException() {
        //act
        HabitEditBindingModel habitEditBindingModel = new HabitEditBindingModel("fitness", FrequencyEnum.DAILY.getFrequencyName(), PriorityEnum.LOW.getPriorityName());

        this.habitService.editHabit(2L, habitEditBindingModel);
    }

    @Test
    public void testStartDateById_givenValidHabit_shouldReturnStartDatey() {
        //act
        LocalDate startDate = this.habitService.getStartDateById(1L);

        //assert
        Assert.assertNotNull("Start date is null after creation", startDate);
        Assert.assertEquals("Wrong start date when loaded by id", fitness.getStartDate(), startDate);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testStartDateById_givenNotValidId_shouldThrowResourceNotFoundException() {
        //act
        this.habitService.getStartDateById(2L);
    }


}