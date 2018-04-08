package org.softuni.habitTracker.areas.activities.controllers;

import org.softuni.habitTracker.areas.activities.models.binding.ActivityAddDTO;
import org.softuni.habitTracker.areas.activities.services.ActivityService;
import org.softuni.habitTracker.areas.habits.services.HabitService;
import org.softuni.habitTracker.areas.logs.annotations.Log;
import org.softuni.habitTracker.areas.users.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.text.ParseException;
import java.time.LocalDate;

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
        modelAndView.addObject("activityViews",
                this.activityService.getAllActivitiesOrderedByDateDesc((User) authentication.getPrincipal()));

        return modelAndView;
    }


    @GetMapping(path = "/add")
    public ModelAndView add(ModelAndView modelAndView, @ModelAttribute("activityAddModel") ActivityAddDTO activityAddDTO,
                            Authentication authentication) throws ParseException {
        modelAndView.setViewName("activities/add");

        modelAndView.addObject("habitViews", this.habitService.getAllHabitsByUserDueToday((User) authentication.getPrincipal()));
        modelAndView.addObject("activityAddModel", activityAddDTO);
        return modelAndView;
    }

    @Log
    @PostMapping(path = "/add/{id}")
    public ModelAndView add(ModelAndView modelAndView, @PathVariable("id") Long id,
                            @Valid @ModelAttribute("activityAddModel") ActivityAddDTO activityAddDTO,
                            BindingResult bindingResult, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            activityAddDTO.setDate(LocalDate.now());
        }

        activityAddDTO.setUser((User) authentication.getPrincipal());
        activityAddDTO.setHabit(this.habitService.getHabitById(id));

        this.activityService.saveActivity(activityAddDTO);

        if(activityAddDTO.getHabit().getNextDueDate() == null){
            redirectAttributes.addFlashAttribute("habit-completed", true);
        }
            modelAndView.setViewName("redirect:/habits/view/" + id);
        return modelAndView;
    }
}
