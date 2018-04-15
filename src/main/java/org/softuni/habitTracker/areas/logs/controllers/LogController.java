package org.softuni.habitTracker.areas.logs.controllers;

import org.softuni.habitTracker.areas.logs.services.LogService;
import org.softuni.habitTracker.controllers.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LogController extends BaseController{
    private final LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/logs")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView showLogs() {
        return super.view("app/logs",
                "logViews",
                this.logService.getAllLogsOrderedByDateDesc());
    }
}
