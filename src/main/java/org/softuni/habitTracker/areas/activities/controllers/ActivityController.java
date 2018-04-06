package org.softuni.habitTracker.areas.activities.controllers;

import org.softuni.habitTracker.areas.activities.models.binding.ActivityAddDTO;
import org.softuni.habitTracker.areas.activities.models.view.ActivityViewDTO;
import org.softuni.habitTracker.areas.activities.services.ActivityService;
import org.softuni.habitTracker.areas.habits.models.view.HabitViewDTO;
import org.softuni.habitTracker.areas.habits.services.HabitService;
import org.softuni.habitTracker.areas.users.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.ParseException;
import java.time.LocalDate;
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

        List<ActivityViewDTO> activityViews = this.activityService.getAllActivitiesOrderedByDateDesc(user);
        modelAndView.addObject("activityViews", activityViews);

        return modelAndView;
    }


    @GetMapping(path = "/add")
    public ModelAndView add(ModelAndView modelAndView, @ModelAttribute("activityAddModel") ActivityAddDTO activityAddDTO,
                            Authentication authentication) throws ParseException {
        modelAndView.setViewName("activities/add");
        User user = (User) authentication.getPrincipal();
        activityAddDTO.setDate(LocalDate.now());

        List<HabitViewDTO> habitViews = this.habitService.getAllHabitsByUserDueToday(user);
        modelAndView.addObject("habitViews", habitViews);
        modelAndView.addObject("activityAddModel", activityAddDTO);
        return modelAndView;
    }

    @PostMapping(path = "/add/{id}")
    public ModelAndView add(ModelAndView modelAndView, @PathVariable("id") Long id,
                            @Valid @ModelAttribute("activityAddModel") ActivityAddDTO activityAddDTO,
                            BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            activityAddDTO.setDate(LocalDate.now());
        }
        User user = (User) authentication.getPrincipal();
        activityAddDTO.setUser(user);
        activityAddDTO.setHabit(this.habitService.getHabitById(id));

        this.activityService.saveActivity(activityAddDTO);
        modelAndView.setViewName("redirect:/habits/view/" + id);
        return modelAndView;
    }
}
