package com.elena.habitTracker.areas.logs.controllers;

import com.elena.habitTracker.areas.logs.services.LogService;
import com.elena.habitTracker.areas.logs.util.Constants;
import com.elena.habitTracker.controllers.BaseController;
import org.omg.CORBA.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LogController extends BaseController {
    private final LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/logs")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView showLogs(@PageableDefault(size = Constants.DEFAULT_LOGS_PER_PAGE)Pageable pageable) {
        return super.view("app/logs",
                "logsPageModel", this.logService.getAllByPage(pageable),
                "page", pageable.getPageNumber() );
    }
}
