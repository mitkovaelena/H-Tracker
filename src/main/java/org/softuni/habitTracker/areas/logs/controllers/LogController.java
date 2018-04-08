package org.softuni.habitTracker.areas.logs.controllers;

import org.softuni.habitTracker.areas.logs.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LogController {
    private LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/logs")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView showLogs(ModelAndView modelAndView) {
        modelAndView.setViewName("app/logs");
        modelAndView.addObject("logViews",
                this.logService.getAllLogsOrderedByDateDesc());


        return modelAndView;
    }
}
