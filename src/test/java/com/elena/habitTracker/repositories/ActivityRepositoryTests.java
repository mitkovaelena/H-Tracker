package com.elena.habitTracker.repositories;

import com.elena.habitTracker.areas.activities.entities.Activity;
import com.elena.habitTracker.areas.activities.models.view.ActivityStaticticsViewModel;
import com.elena.habitTracker.areas.activities.repositories.ActivityRepository;
import com.elena.habitTracker.areas.habits.entities.Habit;
import com.elena.habitTracker.areas.habits.enums.FrequencyEnum;
import com.elena.habitTracker.areas.habits.enums.PriorityEnum;
import com.elena.habitTracker.areas.users.entities.User;
import com.elena.habitTracker.util.TestsUtils;
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

    private Random rnd = new Random();

    private User eli;
    private User vili;
    private Habit fitness;
    private Habit cleanEating;
    private Long activitiesTodayCount;


    @Before
    public void setUp() {
        //arange
        this.eli = new User("eli123", "123456", "eli123@gmail.com", "Elena", "Nikolova");
        this.testEntityManager.persistAndFlush(eli);

        this.fitness = new Habit("fitness", FrequencyEnum.DAILY, PriorityEnum.LOW, LocalDate.now(),eli);
        this.testEntityManager.persistAndFlush(fitness);

        activitiesTodayCount = 0L;
        for (int i = 0; i < rnd.nextInt(100); i++) {
            Activity activity = new Activity(LocalDate.now(), eli, fitness);
            this.testEntityManager.persistAndFlush(activity);
            activitiesTodayCount++;
        }
    }

    @Test
    public void testFindActivitiesCountForDate_givenOnlyActivitiesForToday_shouldFindCorrectCount() {
        //act
        Long result = this.activityRepository.findActivitiesCountForDate(eli, fitness, LocalDate.now());

        //assert
        assertEquals("Wrong count for activities done today", result, activitiesTodayCount);
    }

    @Test
    public void testFindActivitiesCountForDate_givenActivitiesForDifferentDays_shouldFindCorrectCount() {
        //arrange
        for (int i = 1; i < rnd.nextInt(100); i++) {
            Activity activity = new Activity(LocalDate.now().plusDays(i), eli, fitness);
            this.testEntityManager.persistAndFlush(activity);
        }

        //act
        Long result = this.activityRepository.findActivitiesCountForDate(eli, fitness, LocalDate.now());

        if(result == null) result = 0L;

        //assert
        assertEquals("Wrong count for activities done today", result, activitiesTodayCount);
    }

    @Test
    public void testFindActivitiesCountForDate_givenMoreUsersAndHabitsForDifferentDays_shouldFindCorrectCount() {
        this.vili = new User("vili321", "654321", "vili123@gmail.com", "Violeta", "Nikolova");
        ;
        this.testEntityManager.persistAndFlush(vili);

        this.cleanEating =  new Habit("clean eating", FrequencyEnum.DAILY, PriorityEnum.LOW, LocalDate.now(), vili);
        this.testEntityManager.persistAndFlush(cleanEating);

        for (int i = 0; i < rnd.nextInt(100); i++) {
            Activity activity =new Activity(LocalDate.now().plusDays(i), vili, cleanEating);
            this.testEntityManager.persistAndFlush(activity);

        }

        //act
        Long result = this.activityRepository.findActivitiesCountForDate(eli, fitness, LocalDate.now());

        //assert
        assertEquals("Wrong count for activities done today", result, activitiesTodayCount);
    }

    @Test
    public void testFindActivitiesStatistics_givenOnlyActivitiesForToday_shouldFindCorrectCount() {
        //arrange
        this.testFindActivitiesCountForDate_givenOnlyActivitiesForToday_shouldFindCorrectCount();

        //act
        List<ActivityStaticticsViewModel> result = this.activityRepository.findActivitiesStatistics(eli, fitness);

        //assert
        assertEquals("Wrong count for activities done today", result.get(0).getCount(), activitiesTodayCount);
        assertEquals("Wrong date for activities done today", result.get(0).getDate(), LocalDate.now());
    }

    @Test
    public void testFindActivitiesStatistics_givenActivitiesForDifferentDays_shouldFindCorrectCount() {
        //arrange
        this.testFindActivitiesCountForDate_givenActivitiesForDifferentDays_shouldFindCorrectCount();

        //act
        List<ActivityStaticticsViewModel> result = this.activityRepository.findActivitiesStatistics(eli, fitness);

        //assert
        result.sort((x, y) -> x.getDate().isBefore(y.getDate()) ? 1 : 0);
        assertEquals("Wrong count for activities done today", result.get(0).getCount(), activitiesTodayCount);
        assertEquals("Wrong date for activities done today", result.get(0).getDate(), LocalDate.now());

        for (int i = 1; i < result.size(); i++) {
            assertEquals("Wrong count for activities done " + result.get(i).getDate(), result.get(i).getCount(), Long.valueOf(1L));
            assertEquals("Wrong date for activities done on ", result.get(i).getDate(), LocalDate.now().plusDays(i));
        }
    }

    @Test
    public void testFindActivitiesStatistics_givenMoreUsersAndHabitsForDifferentDays_shouldFindCorrectCount() {
        //arrange
        this.testFindActivitiesCountForDate_givenMoreUsersAndHabitsForDifferentDays_shouldFindCorrectCount();

        //act
        List<ActivityStaticticsViewModel> result = this.activityRepository.findActivitiesStatistics(vili, cleanEating);

        //assert
        result.sort((x, y) -> x.getDate().isBefore(y.getDate()) ? 1 : 0);

        for (int i = 0; i < result.size(); i++) {
            assertEquals("Wrong count for activities done " + result.get(i).getDate(), result.get(i).getCount(), Long.valueOf(1L));
            assertEquals("Wrong date for activities done on " + result.get(i).getDate(), result.get(i).getDate(), LocalDate.now().plusDays(i));
        }
    }
}
