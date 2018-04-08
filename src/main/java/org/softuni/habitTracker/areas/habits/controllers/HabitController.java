package org.softuni.habitTracker.areas.habits.controllers;

import org.softuni.habitTracker.areas.habits.enums.FrequencyEnum;
import org.softuni.habitTracker.areas.habits.enums.PriorityEnum;
import org.softuni.habitTracker.areas.habits.models.binding.HabitAddDTO;
import org.softuni.habitTracker.areas.habits.models.binding.HabitEditDTO;
import org.softuni.habitTracker.areas.habits.models.view.HabitViewDTO;
import org.softuni.habitTracker.areas.habits.services.HabitService;
import org.softuni.habitTracker.areas.habits.util.Constants;
import org.softuni.habitTracker.areas.logs.annotations.Log;
import org.softuni.habitTracker.areas.users.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/habits")
@PreAuthorize("isAuthenticated()")
public class HabitController {
    private final HabitService habitService;

    @Autowired
    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @GetMapping("/all")
    public ModelAndView all(ModelAndView modelAndView, Authentication authentication) {
        modelAndView.setViewName("habits/all");
        User user = (User) authentication.getPrincipal();

        List<HabitViewDTO> habitViews = this.habitService.getAllHabitsByUser(user);
        modelAndView.addObject("habitViews", habitViews);

        return modelAndView;
    }


    @GetMapping(path = "/add")
    public ModelAndView add(ModelAndView modelAndView, @ModelAttribute("habitAddModel") HabitAddDTO habitAddDTO) {
        modelAndView.setViewName("habits/add");
        modelAndView.addObject("habitAddModel", habitAddDTO);
        modelAndView.addObject("frequencies", Stream.of(FrequencyEnum.values())
                .map(FrequencyEnum::getFrequencyName).collect(Collectors.toList()));
        modelAndView.addObject("priorities", Stream.of(PriorityEnum.values())
                .map(PriorityEnum::getPriorityName).collect(Collectors.toList()));
        habitAddDTO.setStartDate(LocalDate.now());
        return modelAndView;
    }

    @Log
    @PostMapping(path = "/add")
    public ModelAndView add(ModelAndView modelAndView, @Valid @ModelAttribute("habitAddModel") HabitAddDTO habitAddDTO,
                            BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            if (bindingResult.hasGlobalErrors()) {
                bindingResult.rejectValue("endDate", "error.user", Constants.INVALID_DATE);
            }
            modelAndView.setViewName("habits/add");
            modelAndView.addObject("frequencies", Stream.of(FrequencyEnum.values())
                    .map(FrequencyEnum::getFrequencyName).collect(Collectors.toList()));
            modelAndView.addObject("priorities", Stream.of(PriorityEnum.values())
                    .map(PriorityEnum::getPriorityName).collect(Collectors.toList()));
            return modelAndView;
        }

        User user = (User) authentication.getPrincipal();
        habitAddDTO.setUser(user);
        this.habitService.saveHabit(habitAddDTO);
        modelAndView.setViewName("redirect:/habits/all");
        return modelAndView;
    }

    @GetMapping(path = "/view/{id}")
    public ModelAndView view(ModelAndView modelAndView, @PathVariable("id") Long id) {
        HabitViewDTO habitViewDTO = this.habitService.getHabitViewDTOById(id);
        modelAndView.setViewName("habits/view");
        modelAndView.addObject("habitViewModel", habitViewDTO);
        modelAndView.addObject("lineChartData", this.habitService.extractLineChartData(id));
        return modelAndView;
    }

    @GetMapping(path = "/edit/{id}")
    public ModelAndView edit(ModelAndView modelAndView, @PathVariable("id") Long id) {
        HabitEditDTO habitEditDTO = this.habitService.getHabitEditDTOById(id);
        modelAndView.setViewName("habits/edit");
        modelAndView.addObject("startDate", this.habitService.getStartDateById(id));
        modelAndView.addObject("habitEditModel", habitEditDTO);
        modelAndView.addObject("frequencies", Stream.of(FrequencyEnum.values())
                .map(FrequencyEnum::getFrequencyName).collect(Collectors.toList()));
        modelAndView.addObject("priorities", Stream.of(PriorityEnum.values())
                .map(PriorityEnum::getPriorityName).collect(Collectors.toList()));
        return modelAndView;
    }

    @Log
    @PostMapping(path = "/edit/{id}")
    public ModelAndView edit(ModelAndView modelAndView, @Valid @ModelAttribute("habitEditModel") HabitEditDTO habitEditDTO,
                             BindingResult bindingResult, Authentication authentication, @PathVariable("id") Long id) {

        LocalDate startDate = this.habitService.getStartDateById(id);

        if ((habitEditDTO.getEndDate() != null && habitEditDTO.getEndDate().isBefore(startDate)) || bindingResult.hasErrors()) {
            bindingResult.rejectValue("endDate", "error.user", Constants.INVALID_DATE);
            modelAndView.setViewName("habits/edit");
            modelAndView.addObject("startDate", startDate);
            modelAndView.addObject("frequencies", Stream.of(FrequencyEnum.values())
                    .map(FrequencyEnum::getFrequencyName).collect(Collectors.toList()));
            modelAndView.addObject("priorities", Stream.of(PriorityEnum.values())
                    .map(PriorityEnum::getPriorityName).collect(Collectors.toList()));
            return modelAndView;
        }

        this.habitService.editHabit(id, habitEditDTO);
        modelAndView.setViewName("redirect:/habits/all");
        return modelAndView;
    }

    @Log
    @PostMapping(path = "/renew/{id}")
    public ModelAndView renew(ModelAndView modelAndView, @PathVariable("id") Long id, Authentication authentication) {
        this.habitService.renewHabit(id);
        modelAndView.setViewName("redirect:/habits/view/" + id);
        return modelAndView;
    }

    @Log
    @PostMapping("delete/{id}")
    public ModelAndView delete(ModelAndView modelAndView, @PathVariable("id") Long id) {
        this.habitService.deleteHabit(id);

        modelAndView.setViewName("redirect:/habits/all");
        return modelAndView;
    }
}
