package com.elena.habitTracker.repositories;

import com.elena.habitTracker.areas.activities.entities.Activity;
import com.elena.habitTracker.areas.activities.models.view.ActivityStaticticsViewModel;
import com.elena.habitTracker.areas.activities.repositories.ActivityRepository;
import com.elena.habitTracker.areas.habits.entities.Habit;
import com.elena.habitTracker.areas.habits.enums.FrequencyEnum;
import com.elena.habitTracker.areas.habits.enums.PriorityEnum;
import com.elena.habitTracker.areas.habits.repositories.HabitRepository;
import com.elena.habitTracker.areas.users.entities.User;
import com.elena.habitTracker.areas.users.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
@ActiveProfiles("test")
public class ActivityRepositoryTests {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HabitRepository habitRepository;

    private Random rnd = new Random();
    private User eli;
    private Habit fitness;

    @Before
    public void setUp() {
        User eli = new User();
        eli.setUsername("eli123");
        eli.setPassword("123456");
        eli.setEmail("eli123@gmail.com");

        this.testEntityManager.persistAndFlush(eli);

        Habit fitness = new Habit();
        fitness.setTitle("fitness");
        fitness.setFrequency(FrequencyEnum.DAILY);
        fitness.setPriority(PriorityEnum.LOW);
        fitness.setStartDate(LocalDate.now());
        fitness.setUser(eli);

        this.testEntityManager.persistAndFlush(fitness);

    }

    @Test
    public void testFindActivitiesCountForDate_givenOnlyHabitsForToday_shouldFindCorrectCount(){
        //arrange
        Long activitiesTodayCount = 0L;
        for (int i = 0; i < rnd.nextInt(100); i++) {
            Activity activity = new Activity();
            activity.setDate(LocalDate.now());
            activity.setUser(eli);
            activity.setHabit(fitness);
            this.testEntityManager.persistAndFlush(activity);
            activitiesTodayCount++;
        }

        //act
        Long result = this.activityRepository.findActivitiesCountForDate(eli, fitness, LocalDate.now());

        //assert
        assertEquals("Wrong count for activities done today", result, activitiesTodayCount);
    }

    @Test
    public void testFindActivitiesCountForDate_givenHabitsForDifferentDays_shouldFindCorrectCount(){
        Long activitiesTodayCount = 0L;
        for (int i = 0; i < rnd.nextInt(100); i++) {
            Activity activity = new Activity();
            activity.setDate(LocalDate.now());
            activity.setUser(eli);
            activity.setHabit(fitness);
            this.testEntityManager.persistAndFlush(activity);
            activitiesTodayCount++;
        }

        for (int i = 1; i < rnd.nextInt(100); i++) {
            Activity activity = new Activity();
            activity.setDate(LocalDate.now().plusDays(i));
            activity.setUser(eli);
            activity.setHabit(fitness);
            this.testEntityManager.persistAndFlush(activity);
        }

        //act
        Long result = this.activityRepository.findActivitiesCountForDate(eli, fitness, LocalDate.now());

        //assert
        assertEquals("Wrong count for activities done today", result, activitiesTodayCount);
    }

}
