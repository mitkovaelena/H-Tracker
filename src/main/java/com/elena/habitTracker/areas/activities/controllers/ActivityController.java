package com.elena.habitTracker.areas.activities.controllers;

import com.elena.habitTracker.controllers.BaseController;
import com.elena.habitTracker.areas.activities.models.binding.ActivityAddBindingModel;
import com.elena.habitTracker.areas.activities.services.ActivityService;
import com.elena.habitTracker.areas.habits.services.HabitService;
import com.elena.habitTracker.areas.logs.annotations.Log;
import com.elena.habitTracker.areas.users.entities.User;
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
public class ActivityController extends BaseController {
    private final ActivityService activityService;
    private final HabitService habitService;

    @Autowired
    public ActivityController(ActivityService activityService, HabitService habitService) {
        this.activityService = activityService;
        this.habitService = habitService;
    }

    @GetMapping("/all")
    public ModelAndView all(Authentication authentication) {
        return super.view("activities/all",
                "activityViews",
                this.activityService.getAllActivitiesOrderedByDateDesc((User) authentication.getPrincipal()));
    }

    @GetMapping(path = "/add")
    public ModelAndView add(@ModelAttribute("activityAddModel") ActivityAddBindingModel activityAddBindingModel,
                            Authentication authentication) throws ParseException {
        return super.view("activities/add",
                "habitViews", this.habitService.getAllHabitsByUserDueToday((User) authentication.getPrincipal()),
                "activityAddModel", activityAddBindingModel);
    }

    @Log
    @PostMapping(path = "/add/{id}")
    @PreAuthorize("@accessService.hasAccess(authentication, #id)")
    public ModelAndView add(@PathVariable("id") Long id,
                            @Valid @ModelAttribute("activityAddModel") ActivityAddBindingModel activityAddBindingModel,
                            BindingResult bindingResult, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            activityAddBindingModel.setDate(LocalDate.now());
        }

        activityAddBindingModel.setUser((User) authentication.getPrincipal());
        activityAddBindingModel.setHabit(this.habitService.getHabitById(id));
        this.activityService.saveActivity(activityAddBindingModel);

        if (activityAddBindingModel.getHabit().getNextDueDate() == null) {
            redirectAttributes.addFlashAttribute("habitCompleted", true);
        }
        return super.redirect("/habits/view/" + id);
    }
}
