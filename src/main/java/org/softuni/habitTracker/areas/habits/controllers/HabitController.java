package org.softuni.habitTracker.areas.habits.controllers;

import org.softuni.habitTracker.areas.habits.enums.FrequencyEnum;
import org.softuni.habitTracker.areas.habits.enums.PriorityEnum;
import org.softuni.habitTracker.areas.habits.models.binding.HabitAddBindingModel;
import org.softuni.habitTracker.areas.habits.models.binding.HabitEditBindingModel;
import org.softuni.habitTracker.areas.habits.services.HabitService;
import org.softuni.habitTracker.areas.habits.util.Constants;
import org.softuni.habitTracker.areas.logs.annotations.Log;
import org.softuni.habitTracker.areas.users.entities.User;
import org.softuni.habitTracker.controllers.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @GetMapping("/all")
    public ModelAndView all(Authentication authentication) {
        return super.view("habits/all",
                "habitViews",
                this.habitService.getAllHabitsByUser((User) authentication.getPrincipal()));
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

    @GetMapping(path = "/view/{id}")
    public ModelAndView view(@PathVariable("id") Long id, @ModelAttribute("habitCompleted") Object habitCompleted) {
        return super.view("habits/view",
                "habitCompleted", habitCompleted,
                "habitViewModel", this.habitService.getHabitViewDTOById(id));
    }

    @GetMapping(value = "/view/{id}/statistics", produces = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String viewStatistics(@PathVariable Long id) {
        return this.habitService.extractLineChartData(id);
    }

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
        return super.redirect("/habits/all");
    }

    @Log
    @PostMapping(path = "/renew/{id}")
    public ModelAndView renew(@PathVariable("id") Long id) {
        this.habitService.renewHabit(id);
        return super.redirect("/habits/view/" + id);
    }

    @Log
    @PostMapping("delete/{id}")
    public ModelAndView delete(@PathVariable("id") Long id) {
        this.habitService.deleteHabit(id);
        return super.redirect("/habits/all");
    }
}
