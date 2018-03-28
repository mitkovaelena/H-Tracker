package org.softuni.habitTracker.controllers;

import org.softuni.habitTracker.domain.entities.User;
import org.softuni.habitTracker.domain.models.binding.ActivityAddDTO;
import org.softuni.habitTracker.domain.models.view.ActivityViewDTO;
import org.softuni.habitTracker.domain.models.view.HabitViewDTO;
import org.softuni.habitTracker.services.ActivityService;
import org.softuni.habitTracker.services.HabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/activities")
@PreAuthorize("isAuthenticated()")
public class ActivityController {
    private final ActivityService activityService;
    private final HabitService habitService;

    @Autowired
    public ActivityController(ActivityService activityService, HabitService habitService) {
        this.activityService = activityService;
        this.habitService = habitService;
    }

    @GetMapping("/all")
    public ModelAndView all(ModelAndView modelAndView, Authentication authentication) {
        modelAndView.setViewName("activities/all");
        User user = (User) authentication.getPrincipal();

        List<ActivityViewDTO> activityViews = this.activityService.findAllActivities(user);
        modelAndView.addObject("activityViews", activityViews);

        return modelAndView;
    }


    @GetMapping(path = "/add")
    public ModelAndView add(ModelAndView modelAndView, Authentication authentication) {
        modelAndView.setViewName("activities/add");
        User user = (User) authentication.getPrincipal();

        List<HabitViewDTO> habitViews = this.habitService.findAllHabits(user);
        modelAndView.addObject("habitViews", habitViews);
        return modelAndView;
    }

    @PostMapping(path = "/add/{id}")
    public ModelAndView add(ModelAndView modelAndView, @PathVariable("id") Long id, Authentication authentication) {
        ActivityAddDTO activityAddDTO = new ActivityAddDTO();
        User user = (User) authentication.getPrincipal();
        activityAddDTO.setUser(user);
        activityAddDTO.setHabit(this.habitService.getHabitById(id));
        activityAddDTO.setDate(new Date());  //Todo
        this.activityService.saveActivity(activityAddDTO);
        modelAndView.setViewName("redirect:/activities/all");
        return modelAndView;
    }
}
