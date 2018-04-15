package com.elena.habitTracker.areas.logs.services;

import com.elena.habitTracker.areas.logs.models.view.LogsPageViewModel;
import org.modelmapper.ModelMapper;
import com.elena.habitTracker.areas.logs.entities.ApplicationLog;
import com.elena.habitTracker.areas.logs.models.view.ApplicationLogViewModel;
import com.elena.habitTracker.areas.logs.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class LogServiceImpl implements LogService {
    private final LogRepository logRepository;
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

    @Override
    public LogsPageViewModel getAllByPage(Pageable pageable){
        LogsPageViewModel logsPageViewModel = new LogsPageViewModel();

        logsPageViewModel.setLogs(this.logRepository.findAllByOrderByTimeDesc(pageable));
        logsPageViewModel.setTotalPagesCount(this.getTotalPages());

        return logsPageViewModel;
    }

    @Override
    public long getTotalPages(int size) {
        return this.logRepository.count() / size;
    }
}
