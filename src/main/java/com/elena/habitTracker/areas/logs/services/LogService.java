package com.elena.habitTracker.areas.logs.services;

import com.elena.habitTracker.areas.logs.entities.ApplicationLog;
import com.elena.habitTracker.areas.logs.models.view.ApplicationLogsPageViewModel;
import org.springframework.data.domain.Pageable;

public interface LogService {
    ApplicationLog saveLog(ApplicationLog log);

    ApplicationLogsPageViewModel getAllByPage(Pageable pageable);
}
