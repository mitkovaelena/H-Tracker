package org.softuni.habitTracker.controllers;

import org.softuni.habitTracker.domain.entities.User;
import org.softuni.habitTracker.domain.models.binding.HabitAddDTO;
import org.softuni.habitTracker.domain.models.binding.HabitEditDTO;
import org.softuni.habitTracker.domain.models.view.HabitViewDTO;
import org.softuni.habitTracker.services.HabitService;
import org.softuni.habitTracker.util.Constants;
import org.softuni.habitTracker.util.enums.FrequencyEnum;
import org.softuni.habitTracker.util.enums.PriorityEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Date;
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

        List<HabitViewDTO> habitViews = this.habitService.findAllHabits(user);
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
        habitAddDTO.setStartDate(new Date());
        return modelAndView;
    }

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

    @PostMapping(path = "/edit/{id}")
    public ModelAndView edit(ModelAndView modelAndView, @Valid @ModelAttribute("habitEditModel") HabitEditDTO habitEditDTO,
                             BindingResult bindingResult, Authentication authentication, @PathVariable("id") Long id) {

        Date startDate = this.habitService.getStartDateById(id);

        if ((habitEditDTO.getEndDate() != null && habitEditDTO.getEndDate().before(startDate)) || bindingResult.hasErrors()) {
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

    @PostMapping("delete/{id}")
    public ModelAndView delete(ModelAndView modelAndView, @PathVariable("id") Long id) {
        this.habitService.deleteHabit(id);

        modelAndView.setViewName("redirect:/habits/all");
        return modelAndView;
    }
}
