package com.elena.habitTracker.services;

import com.elena.habitTracker.areas.activities.entities.Activity;
import com.elena.habitTracker.areas.activities.models.binding.ActivityAddBindingModel;
import com.elena.habitTracker.areas.activities.models.view.ActivitiesPageViewModel;
import com.elena.habitTracker.areas.activities.models.view.ActivityViewModel;
import com.elena.habitTracker.areas.activities.repositories.ActivityRepository;
import com.elena.habitTracker.areas.activities.services.ActivityService;
import com.elena.habitTracker.areas.activities.services.ActivityServiceImpl;
import com.elena.habitTracker.areas.habits.entities.Habit;
import com.elena.habitTracker.areas.habits.repositories.HabitRepository;
import com.elena.habitTracker.areas.users.entities.User;
import com.elena.habitTracker.util.TestsUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ActivityServiceTests {
    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private HabitRepository habitRepository;

    private ActivityService activityService;

    private ActivityAddBindingModel activityModel;

    private User eli;

    private Habit fitness;

    private PageRequest pagable;

    @Before
    public void setUp() {
        activityService = new ActivityServiceImpl(activityRepository, habitRepository, new ModelMapper());

        this.eli = TestsUtils.createUserEli();
        this.fitness = TestsUtils.createHabitFitness(eli);

        pagable = PageRequest.of(1, 2);

        this.activityModel = TestsUtils.createActivityAddBindingModel(eli, fitness, LocalDate.now());

        when(this.activityRepository.save(any()))
                .thenAnswer(a -> a.getArgument(0));

        when(this.habitRepository.save(any()))
                .thenAnswer(a -> a.getArgument(0));

    }

    @Test
    public void testSaveActivity_givenValidActivity_shouldNotReturnNull() {
        //act
        Activity createdActivity = this.activityService.saveActivity(this.activityModel);

        //assert
        Assert.assertNotNull("Activity is null after creation", createdActivity);
    }

    @Test
    public void testSaveActivity_givenValidActivity_shouldMapFieldsRight() {
        //act
        Activity createdActivity = this.activityService.saveActivity(this.activityModel);

        //assert
        Assert.assertEquals("Wrong habit after activity is created", activityModel.getHabit(), createdActivity.getHabit());
        Assert.assertEquals("Wrong user after activity is created", activityModel.getUser(), createdActivity.getUser());
        Assert.assertEquals("Wrong start date after activity is created", activityModel.getDate(), createdActivity.getDate());
        Assert.assertEquals("Wrong next due date after activity is created", activityModel.getHabit().calculateNextDueDate(), createdActivity.getHabit().getNextDueDate());
    }

    @Test
    public void testSaveActivity_CompletedOnDueDate_shouldIncrementStreak() {
        //arrange
        this.activityModel.getHabit().setNextDueDate(LocalDate.now());
        this.activityModel.setDate(LocalDate.now());

        int prevStreak = this.activityModel.getHabit().getStreak();

        //act
        Activity createdActivity = this.activityService.saveActivity(this.activityModel);

        //assert
        Assert.assertEquals("Streak is not incremented when activity is done on due date",
                prevStreak + 1, createdActivity.getHabit().getStreak());
    }

    @Test
    public void testGetAllActivitiesOrderedByDateDesc_givenValidActivities_shouldMapCorrectly() {
        //arrange
        List<Activity> activities = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Activity activity = TestsUtils.createActivity(eli, fitness, LocalDate.now());
            ActivityAddBindingModel activityAddBindingModel = TestsUtils.createActivityAddBindingModel(eli, fitness, LocalDate.now());
            activities.add(activity);
            this.activityService.saveActivity(activityAddBindingModel);
        }

        when(this.activityRepository.findAllByUserOrderByDateDesc(eli, pagable))
                .thenAnswer(a -> new PageImpl<Activity>(activities, pagable, activities.size()));

        //act
        ActivitiesPageViewModel pageViewModel = this.activityService.getAllActivitiesOrderedByDateDesc(this.eli, pagable);

        //assert
        Assert.assertNotNull("Page is null after creation", pageViewModel);
        Assert.assertNotNull("Activities in page are null after creation", pageViewModel.getActivities().getContent());

        for (int i = 0; i < pageViewModel.getActivities().getContent().size(); i++) {
            Assert.assertEquals("Dates in page differ", activities.get(i).getDate(), pageViewModel.getActivities().getContent().get(i).getDate());
            Assert.assertEquals("Users in page differ", activities.get(i).getHabit().getTitle(), pageViewModel.getActivities().getContent().get(i).getHabit().getTitle());
        }

    }
}
