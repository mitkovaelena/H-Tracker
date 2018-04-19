package com.elena.habitTracker.areas.activities.models.view;

import java.time.LocalDate;

public class ActivityStaticticsViewModel {
    private LocalDate date;
    private Long count;

    public ActivityStaticticsViewModel(LocalDate date, long count) {
        this.date = date;
        this.count = count;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
