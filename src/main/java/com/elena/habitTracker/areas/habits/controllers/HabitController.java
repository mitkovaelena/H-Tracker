package com.elena.habitTracker.areas.habits.controllers;

import com.elena.habitTracker.areas.habits.enums.FrequencyEnum;
import com.elena.habitTracker.areas.habits.enums.PriorityEnum;
import com.elena.habitTracker.areas.habits.models.binding.HabitAddBindingModel;
import com.elena.habitTracker.areas.habits.models.binding.HabitEditBindingModel;
import com.elena.habitTracker.areas.habits.services.HabitService;
import com.elena.habitTracker.areas.habits.util.Constants;
import com.elena.habitTracker.areas.logs.annotations.Log;
import com.elena.habitTracker.areas.users.entities.User;
import com.elena.habitTracker.areas.users.services.UserService;
import com.elena.habitTracker.controllers.BaseController;
import com.elena.habitTracker.util.ApplicationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/habits")
@PreAuthorize("isAuthenticated()")
public class HabitController extends BaseController {
    private final HabitService habitService;
    private final UserService userService;

    @Autowired
    public HabitController(HabitService habitService, UserService userService) {
        this.habitService = habitService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ModelAndView all(@PageableDefault(size = ApplicationConstants.DEFAULT_VIEWS_COUNT_PER_PAGE) Pageable pageable, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return this.all(user.getId(), pageable);
    }

    @GetMapping("/all/{id}")
    @PreAuthorize("@accessService.hasAccess(authentication, #id)")
    public ModelAndView all(@PathVariable Long id,
                            @PageableDefault(size = ApplicationConstants.DEFAULT_VIEWS_COUNT_PER_PAGE) Pageable pageable) {
        return super.view("habits/all",
                "habitsPageModel",
                this.habitService.getHabitsPageByUser(this.userService.getUserById(id), pageable),
                "page", pageable.getPageNumber());
    }

    @GetMapping(path = "/add")
    public ModelAndView add(@ModelAttribute("habitAddModel") HabitAddBindingModel habitAddBindingModel) {
        return super.view("habits/add",
                "habitAddModel", habitAddBindingModel,
                "frequencies", Stream.of(FrequencyEnum.values())
                        .map(FrequencyEnum::getFrequencyName).collect(Collectors.toList()),
                "priorities", Stream.of(PriorityEnum.values())
                        .map(PriorityEnum::getPriorityName).collect(Collectors.toList()));
    }

    @Log
    @PostMapping(path = "/add")
    public ModelAndView add(@Valid @ModelAttribute("habitAddModel") HabitAddBindingModel habitAddBindingModel,
                            BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            if (bindingResult.hasGlobalErrors()) {
                bindingResult.rejectValue("endDate", "error.user", Constants.INVALID_DATE);
            }
            return this.add(habitAddBindingModel);
        }

        habitAddBindingModel.setUser((User) authentication.getPrincipal());
        this.habitService.saveHabit(habitAddBindingModel);
        return super.redirect("/habits/all");
    }

    @PreAuthorize("@accessService.hasAccess(authentication, #id)")
    @GetMapping(path = "/view/{id}")
    public ModelAndView view(@PathVariable("id") Long id, @ModelAttribute("habitCompleted") Object habitCompleted) {
        return super.view("habits/view",
                "habitCompleted", habitCompleted,
                "habitViewModel", this.habitService.getHabitViewDTOById(id));
    }

    @GetMapping(value = "/view/{id}/statistics", produces = "application/json")
    @PreAuthorize("@accessService.hasAccess(authentication, #id)")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String viewStatistics(@PathVariable Long id) {
        return this.habitService.extractLineChartData(id);
    }

    @PreAuthorize("@accessService.hasAccess(authentication, #id)")
    @GetMapping(path = "/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id) {
        return super.view("habits/edit",
                "startDate", this.habitService.getStartDateById(id),
                "habitEditModel", this.habitService.getHabitEditDTOById(id),
                "frequencies", Stream.of(FrequencyEnum.values())
                        .map(FrequencyEnum::getFrequencyName).collect(Collectors.toList()),
                "priorities", Stream.of(PriorityEnum.values())
                        .map(PriorityEnum::getPriorityName).collect(Collectors.toList()));
    }

    @Log
    @PreAuthorize("@accessService.hasAccess(authentication, #id)")
    @PostMapping(path = "/edit/{id}")
    public ModelAndView edit(@Valid @ModelAttribute("habitEditModel") HabitEditBindingModel habitEditBindingModel,
                             BindingResult bindingResult, @PathVariable("id") Long id) {
        if ((habitEditBindingModel.getEndDate() != null
                && habitEditBindingModel.getEndDate().isBefore(this.habitService.getStartDateById(id)))
                || bindingResult.hasErrors()) {
            bindingResult.rejectValue("endDate", "error.user", Constants.INVALID_DATE);
            return this.edit(id);
        }

        this.habitService.editHabit(id, habitEditBindingModel);
        return super.redirect("/habits/view/" + id);
    }

    @Log
    @PreAuthorize("@accessService.isOwner(authentication, #id)")
    @PostMapping(path = "/renew/{id}")
    public ModelAndView renew(@PathVariable("id") Long id) {
        this.habitService.renewHabit(id);
        return super.redirect("/habits/view/" + id);
    }

    @Log
    @PreAuthorize("@accessService.hasAccess(authentication, #id)")
    @PostMapping("/delete")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String delete(@RequestParam Long id) {
        this.habitService.deleteHabit(id);
        return "/";
    }
}
