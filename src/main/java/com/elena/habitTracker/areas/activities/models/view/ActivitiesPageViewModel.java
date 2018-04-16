package com.elena.habitTracker.areas.activities.models.view;

import org.springframework.data.domain.Page;

public class ActivitiesPageViewModel {
    private Page<ActivityViewModel> activities;

    private long totalPagesCount;

    public Page<ActivityViewModel> getActivities() {
        return activities;
    }

    public void setActivities(Page<ActivityViewModel> activities) {
        this.activities = activities;
    }

    public long getTotalPagesCount() {
        return totalPagesCount;
    }

    public void setTotalPagesCount(long totalPagesCount) {
        this.totalPagesCount = totalPagesCount;
    }
}
