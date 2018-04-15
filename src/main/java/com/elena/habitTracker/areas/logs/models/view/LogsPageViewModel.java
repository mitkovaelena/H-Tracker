package com.elena.habitTracker.areas.logs.models.view;

import com.elena.habitTracker.areas.logs.entities.ApplicationLog;
import org.springframework.data.domain.Page;

public class LogsPageViewModel {
    private Page<ApplicationLog> logs;

    private long totalPagesCount;

    public Page<ApplicationLog> getLogs() {
        return logs;
    }

    public void setLogs(Page<ApplicationLog> logs) {
        this.logs = logs;
    }

    public long getTotalPagesCount() {
        return totalPagesCount;
    }

    public void setTotalPagesCount(long totalPagesCount) {
        this.totalPagesCount = totalPagesCount;
    }
}
