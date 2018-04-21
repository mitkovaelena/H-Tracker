package com.elena.habitTracker.entities;

import com.elena.habitTracker.areas.habits.entities.Habit;
import com.elena.habitTracker.areas.habits.enums.FrequencyEnum;
import com.elena.habitTracker.areas.habits.enums.PriorityEnum;
import com.elena.habitTracker.areas.users.entities.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class HabitTests {
    private Habit habit;

    @Before
    public void setUp() {
        User eli = new User("eli123", "123456", "eli123@gmail.com", "Elena", "Nikolova");
        this.habit = new Habit("fitness", FrequencyEnum.DAILY, PriorityEnum.LOW, LocalDate.now(), eli);
    }

    @Test
    public void testCalculateNextDueDate_withDailyFrequencyAndNextDueDateToday_shouldReturnTomorrow() {
        //arrange
        habit.setNextDueDate(LocalDate.now());
        habit.setFrequency(FrequencyEnum.DAILY);

        //act
        LocalDate nextDueDate = habit.calculateNextDueDate();

        //assert
        Assert.assertEquals("Next due date is not correct when habit has daily frequency",
                LocalDate.now().plusDays(1), nextDueDate);

    }

    @Test
    public void testCalculateNextDueDate_withDailyFrequencyAndNextDueDateInThePast_shouldReturnTomorrow() {
        //arrange
        habit.setNextDueDate(LocalDate.now().minusDays(7));
        habit.setFrequency(FrequencyEnum.DAILY);

        //act
        LocalDate nextDueDate = habit.calculateNextDueDate();

        //assert
        Assert.assertEquals("Next due date is not correct when habit has daily frequency",
                LocalDate.now().plusDays(1), nextDueDate);

    }

    @Test
    public void testCalculateNextDueDate_withDailyFrequencyAndNextDueDateInTheFuture_shouldNotChange() {
        //arrange
        habit.setNextDueDate(LocalDate.now().plusDays(7));
        habit.setFrequency(FrequencyEnum.DAILY);

        //act
        LocalDate nextDueDate = habit.calculateNextDueDate();

        //assert
        Assert.assertEquals("Next due date is not correct when habit has daily frequency",
                LocalDate.now().plusDays(7), nextDueDate);

    }

    @Test
    public void testCalculateNextDueDate_withFrequencyEveryOtherDayAndNextDueDateToday_shouldWorkCorrectly() {
        //arrange
        habit.setNextDueDate(LocalDate.now());
        habit.setFrequency(FrequencyEnum.EVERY_OTHER_DAY);

        //act
        LocalDate nextDueDate = habit.calculateNextDueDate();

        //assert
        Assert.assertEquals("Next due date is not correct when habit has every other day frequency",
                LocalDate.now().plusDays(2), nextDueDate);

    }

    @Test
    public void testCalculateNextDueDate_withFrequencyEveryOtherDayAndNextDueDateInThePast_shouldWorkCorrectly() {
        //arrange
        habit.setNextDueDate(LocalDate.now().minusDays(6));
        habit.setFrequency(FrequencyEnum.EVERY_OTHER_DAY);

        //act
        LocalDate nextDueDate = habit.calculateNextDueDate();

        //assert
        Assert.assertEquals("Next due date is not correct when habit has every other day frequency",
                LocalDate.now().plusDays(2), nextDueDate);

    }

    @Test
    public void testCalculateNextDueDate_withFrequencyEveryOtherDayAndNextDueDateInTheFuture_shouldNotChange() {
        //arrange
        habit.setNextDueDate(LocalDate.now().plusDays(7));
        habit.setFrequency(FrequencyEnum.EVERY_OTHER_DAY);

        //act
        LocalDate nextDueDate = habit.calculateNextDueDate();

        //assert
        Assert.assertEquals("Next due date is not correct when habit has every other day frequency",
                LocalDate.now().plusDays(7), nextDueDate);

    }

    @Test
    public void testCalculateNextDueDate_withMondayToFridayFrequencyAndNextDueDateFriday_shouldReturnMonday() {
        //arrange
        habit.setNextDueDate(LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.FRIDAY)));
        habit.setFrequency(FrequencyEnum.MONDAY_TO_FRIDAY);

        //act
        LocalDate nextDueDate = habit.calculateNextDueDate();

        //assert
        Assert.assertEquals("Next due date is not correct when habit has monday to friday frequency",
                LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)), nextDueDate);

    }

    @Test
    public void testCalculateNextDueDate_withMondayToFridayFrequencyAndNextDueDateSaturday_shouldReturnMonday() {
        //arrange
        habit.setNextDueDate(LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SATURDAY)));
        habit.setFrequency(FrequencyEnum.MONDAY_TO_FRIDAY);

        //act
        LocalDate nextDueDate = habit.calculateNextDueDate();

        //assert
        Assert.assertEquals("Next due date is not correct when habit has monday to friday frequency",
                LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)), nextDueDate);
    }

    @Test
    public void testCalculateNextDueDate_withMondayToFridayFrequencyAndNextDueDateSunday_shouldReturnMonday() {
        //arrange
        habit.setNextDueDate(LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)));
        habit.setFrequency(FrequencyEnum.MONDAY_TO_FRIDAY);

        //act
        LocalDate nextDueDate = habit.calculateNextDueDate();

        //assert
        Assert.assertEquals("Next due date is not correct when habit has monday to friday frequency",
                LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)), nextDueDate);
    }

    @Test
    public void testCalculateNextDueDate_withFrequencyEveryOtherDayAndNextDueDateIsInTheFuture_shouldNotChange() {
        //arrange
        habit.setNextDueDate(LocalDate.now().plusDays(7));
        habit.setFrequency(FrequencyEnum.MONDAY_TO_FRIDAY);

        //act
        LocalDate nextDueDate = habit.calculateNextDueDate();

        //assert
        Assert.assertEquals("Next due date is not correct when habit has monday to friday frequency",
                LocalDate.now().plusDays(7), nextDueDate);
    }

    @Test
    public void testCalculateNextDueDate_withWeeklyFrequencyAndNextDueDateToday_shouldReturnNextWeek() {
        //arrange
        habit.setNextDueDate(LocalDate.now());
        habit.setFrequency(FrequencyEnum.WEEKLY);

        //act
        LocalDate nextDueDate = habit.calculateNextDueDate();

        //assert
        Assert.assertEquals("Next due date is not correct when habit has weekly frequency",
                LocalDate.now().plusDays(7), nextDueDate);

    }

    @Test
    public void testCalculateNextDueDate_withWeeklyFrequencyAndNextDueDateInThePast_shouldWorkCorrectly() {
        //arrange
        habit.setNextDueDate(LocalDate.now().minusDays(2));
        habit.setFrequency(FrequencyEnum.WEEKLY);

        //act
        LocalDate nextDueDate = habit.calculateNextDueDate();

        //assert
        Assert.assertEquals("Next due date is not correct when habit has weekly frequency",
                LocalDate.now().plusDays(5), nextDueDate);

    }

    @Test
    public void testCalculateNextDueDate_withWeeklyFrequencyAndNextDueDateInTheFuture_shouldNotChange() {
        //arrange
        habit.setNextDueDate(LocalDate.now().plusDays(7));
        habit.setFrequency(FrequencyEnum.WEEKLY);

        //act
        LocalDate nextDueDate = habit.calculateNextDueDate();

        //assert
        Assert.assertEquals("Next due date is not correct when habit has weekly frequency",
                LocalDate.now().plusDays(7), nextDueDate);

    }

    @Test
    public void testCalculateNextDueDate_withMonthlyFrequencyAndNextDueDateToday_shouldReturnNextMonth() {
        //arrange
        habit.setNextDueDate(LocalDate.now());
        habit.setFrequency(FrequencyEnum.MONTHLY);

        //act
        LocalDate nextDueDate = habit.calculateNextDueDate();

        //assert
        Assert.assertEquals("Next due date is not correct when habit has monthly frequency",
                LocalDate.now().plusMonths(1), nextDueDate);

    }

    @Test
    public void testCalculateNextDueDate_withMonthlyFrequencyAndNextDueDateInThePast_shouldWorkCorrectly() {
        //arrange
        habit.setNextDueDate(LocalDate.now().minusMonths(1).minusDays(7));
        habit.setFrequency(FrequencyEnum.MONTHLY);

        //act
        LocalDate nextDueDate = habit.calculateNextDueDate();

        //assert
        Assert.assertEquals("Next due date is not correct when habit has monthly frequency",
                LocalDate.now().minusDays(7).plusMonths(1), nextDueDate);

    }

    @Test
    public void testCalculateNextDueDate_withMonthlyFrequencyAndNextDueDateInTheFuture_shouldNotChange() {
        //arrange
        habit.setNextDueDate(LocalDate.now().plusDays(7));
        habit.setFrequency(FrequencyEnum.MONTHLY);

        //act
        LocalDate nextDueDate = habit.calculateNextDueDate();

        //assert
        Assert.assertEquals("Next due date is not correct when habit has daily frequency",
                LocalDate.now().plusDays(7), nextDueDate);

    }

    @Test
    public void testCalculateNextDueDate_withYearlyFrequencyAndNextDueDateToday_shouldReturnNextYear() {
        //arrange
        habit.setNextDueDate(LocalDate.now());
        habit.setFrequency(FrequencyEnum.YEARLY);

        //act
        LocalDate nextDueDate = habit.calculateNextDueDate();

        //assert
        Assert.assertEquals("Next due date is not correct when habit has yearly frequency",
                LocalDate.now().plusYears(1), nextDueDate);

    }

    @Test
    public void testCalculateNextDueDate_withYearlyFrequencyAndNextDueDateInThePast_shouldWorkCorrectly() {
        //arrange
        habit.setNextDueDate(LocalDate.now().minusDays(7).minusYears(2));
        habit.setFrequency(FrequencyEnum.YEARLY);

        //act
        LocalDate nextDueDate = habit.calculateNextDueDate();

        //assert
        Assert.assertEquals("Next due date is not correct when habit has yearly frequency",
                LocalDate.now().minusDays(7).plusYears(1), nextDueDate);

    }

    @Test
    public void testCalculateNextDueDate_withYearlyFrequencyAndNextDueDateInTheFuture_shouldNotChange() {
        //arrange
        habit.setNextDueDate(LocalDate.now().plusDays(7));
        habit.setFrequency(FrequencyEnum.YEARLY);

        //act
        LocalDate nextDueDate = habit.calculateNextDueDate();

        //assert
        Assert.assertEquals("Next due date is not correct when habit has yearly frequency",
                LocalDate.now().plusDays(7), nextDueDate);
    }

    @Test
    public void testCalculateNextDueDate_withNotCompletedHabit_shouldReturnNullAfterComplwtion() {
        //arrange
        habit.setNextDueDate(LocalDate.now());
        habit.setFrequency(FrequencyEnum.DAILY);
        habit.setEndDate(LocalDate.now());

        //act
        LocalDate nextDueDate = habit.calculateNextDueDate();

        //assert
        Assert.assertNull("Next due date is not null after habit is completed", nextDueDate);
    }

    @Test
    public void testCalculateNextDueDate_withCompletedHabit_shouldReturnNull() {
        //arrange
        habit.setNextDueDate(null);
        habit.setFrequency(FrequencyEnum.DAILY);
        habit.setEndDate(LocalDate.now().minusDays(1));

        //act
        LocalDate nextDueDate = habit.calculateNextDueDate();

        //assert
        Assert.assertNull("Next due date is not null when habit is completed", nextDueDate);
    }
}
