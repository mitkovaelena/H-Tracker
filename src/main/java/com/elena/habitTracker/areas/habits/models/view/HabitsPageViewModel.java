package com.elena.habitTracker.areas.habits.models.view;

import com.elena.habitTracker.areas.logs.entities.ApplicationLog;
import org.springframework.data.domain.Page;

public class HabitsPageViewModel {
    private Page<HabitViewModel> habits;

    private long totalPagesCount;

    public Page<HabitViewModel> getHabits() {
        return habits;
    }

    public void setHabits(Page<HabitViewModel> habits) {
        this.habits = habits;
    }

    public long getTotalPagesCount() {
        return totalPagesCount;
    }

    public void setTotalPagesCount(long totalPagesCount) {
        this.totalPagesCount = totalPagesCount;
    }
}
