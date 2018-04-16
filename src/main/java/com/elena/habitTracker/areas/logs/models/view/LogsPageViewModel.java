package com.elena.habitTracker.areas.logs.models.view;

import org.springframework.data.domain.Page;

public class LogsPageViewModel {
    private Page<ApplicationLogViewModel> logs;

    private long totalPagesCount;

    public Page<ApplicationLogViewModel> getLogs() {
        return logs;
    }

    public void setLogs(Page<ApplicationLogViewModel> logs) {
        this.logs = logs;
    }

    public long getTotalPagesCount() {
        return totalPagesCount;
    }

    public void setTotalPagesCount(long totalPagesCount) {
        this.totalPagesCount = totalPagesCount;
    }
}
