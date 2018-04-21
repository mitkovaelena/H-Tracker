package com.elena.habitTracker.areas.activities.controllers;

import com.elena.habitTracker.areas.activities.models.binding.ActivityAddBindingModel;
import com.elena.habitTracker.areas.activities.services.ActivityService;
import com.elena.habitTracker.areas.habits.services.HabitService;
import com.elena.habitTracker.areas.logs.annotations.Log;
import com.elena.habitTracker.areas.users.entities.User;
import com.elena.habitTracker.areas.users.services.UserService;
import com.elena.habitTracker.controllers.BaseController;
import com.elena.habitTracker.util.ApplicationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    private final UserService userService;
    private final HabitService habitService;

    @Autowired
    public ActivityController(ActivityService activityService, UserService userService, HabitService habitService) {
        this.activityService = activityService;
        this.userService = userService;
        this.habitService = habitService;
    }

    @GetMapping("/all")
    public ModelAndView all(@PageableDefault(size = ApplicationConstants.DEFAULT_VIEWS_COUNT_PER_PAGE) Pageable pageable,
                            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return this.all(user.getId(), pageable);
    }

    @GetMapping("/all/{id}")
    @PreAuthorize("@accessService.hasAccess(authentication, #id)")
    public ModelAndView all(@PathVariable Long id,
                            @PageableDefault(size = ApplicationConstants.DEFAULT_VIEWS_COUNT_PER_PAGE) Pageable pageable) {
        return super.view("activities/all",
                "activityPageModel",
                this.activityService.getAllActivitiesOrderedByDateDesc(this.userService.getUserById(id), pageable),
                "userId", id,
                "page", pageable.getPageNumber());
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
    @PreAuthorize("@accessService.isOwner(authentication, #id)")
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
