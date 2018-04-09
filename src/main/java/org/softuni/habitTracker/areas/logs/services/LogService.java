package org.softuni.habitTracker.areas.logs.services;

import org.softuni.habitTracker.areas.logs.entities.ApplicationLog;
import org.softuni.habitTracker.areas.logs.models.view.ApplicationLogViewModel;

import java.util.List;

public interface LogService {
    void saveLog(ApplicationLog log);

    List<ApplicationLogViewModel> getAllLogsOrderedByDateDesc();
}
