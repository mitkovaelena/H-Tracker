package com.elena.habitTracker.areas.logs.services;

import com.elena.habitTracker.areas.logs.entities.ApplicationLog;
import com.elena.habitTracker.areas.logs.models.view.ApplicationLogViewModel;
import com.elena.habitTracker.areas.logs.models.view.ApplicationLogsPageViewModel;
import com.elena.habitTracker.areas.logs.repositories.LogRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


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
    public ApplicationLog saveLog(ApplicationLog log) {
        return this.logRepository.save(log);
    }

    @Override
    public ApplicationLogsPageViewModel getAllByPage(Pageable pageable) {
        Page<ApplicationLog> applicationLogsPage = this.logRepository.findAllByOrderByTimeDesc(pageable);
        int totalElements = (int) applicationLogsPage.getTotalElements();

        Page<ApplicationLogViewModel> applicationLogViewModelPage = new PageImpl<>(
                applicationLogsPage.stream()
                        .map(log -> this.modelMapper.map(log, ApplicationLogViewModel.class))
                        .collect(Collectors.toList()), pageable, totalElements);

        ApplicationLogsPageViewModel applicationLogsPageViewModel = new ApplicationLogsPageViewModel();
        applicationLogsPageViewModel.setLogs(applicationLogViewModelPage);
        applicationLogsPageViewModel.setTotalPagesCount(applicationLogViewModelPage.getTotalPages());

        return applicationLogsPageViewModel;
    }
}
