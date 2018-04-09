package org.softuni.habitTracker.areas.logs.services;

import org.modelmapper.ModelMapper;
import org.softuni.habitTracker.areas.logs.entities.ApplicationLog;
import org.softuni.habitTracker.areas.logs.models.view.ApplicationLogViewModel;
import org.softuni.habitTracker.areas.logs.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class LogServiceImpl implements LogService {
    private LogRepository logRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public LogServiceImpl(LogRepository logRepository, ModelMapper modelMapper) {
        this.logRepository = logRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void saveLog(ApplicationLog log) {
        this.logRepository.save(log);
    }

    @Override
    public List<ApplicationLogViewModel> getAllLogsOrderedByDateDesc() {
        List<ApplicationLog> logs = this.logRepository.findAllByOrderByTimeDesc();
        List<ApplicationLogViewModel> logViewDTOS = new ArrayList<>();

        for (ApplicationLog log : logs) {
            logViewDTOS.add(modelMapper.map(log, ApplicationLogViewModel.class));
        }

        return logViewDTOS;
    }
}
