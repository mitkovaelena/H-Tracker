package com.elena.habitTracker.areas.logs.services;

import com.elena.habitTracker.areas.logs.entities.ApplicationLog;
import com.elena.habitTracker.areas.logs.models.view.LogsPageViewModel;
import com.elena.habitTracker.areas.logs.models.view.ApplicationLogViewModel;
import com.elena.habitTracker.areas.logs.util.Constants;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LogService {
    void saveLog(ApplicationLog log);

    List<ApplicationLogViewModel> getAllLogsOrderedByDateDesc();

    LogsPageViewModel getAllByPage(Pageable pageable);

    default long getTotalPages() {
        return getTotalPages(Constants.DEFAULT_LOGS_PER_PAGE);
    }

    long getTotalPages(int size);
}
