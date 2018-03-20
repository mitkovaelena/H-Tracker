package org.softuni.habitTracker.controllers;

import org.softuni.habitTracker.domain.entities.User;
import org.softuni.habitTracker.domain.models.binding.HabitAddDTO;
import org.softuni.habitTracker.domain.models.binding.UserRegisterDTO;
import org.softuni.habitTracker.services.HabitService;
import org.softuni.habitTracker.util.Constants;
import org.softuni.habitTracker.util.enums.HabitFrequencyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/habits")
public class HabitController {
    private final HabitService habitService;

    @Autowired
    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @GetMapping(path = "/add")
   // @PreAuthorize("isAuthenticated()")
    public ModelAndView register(ModelAndView modelAndView, @ModelAttribute("habitAddModel") HabitAddDTO habitAddDTO) {
        modelAndView.setViewName("habits/add");
        modelAndView.addObject("habitAddModel", habitAddDTO);
        modelAndView.addObject("frequencies", Stream.of(HabitFrequencyEnum.values())
                .map(HabitFrequencyEnum::getFrequencyName).collect(Collectors.toList()));
        habitAddDTO.setStartDate(new Date());
        return modelAndView;
    }

    @PostMapping(path = "/add")
    //@PreAuthorize("isAuthenticated()") ToDo
    public ModelAndView register(ModelAndView modelAndView, @Valid @ModelAttribute("habitAddModel") HabitAddDTO habitAddDTO,
                                 BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            if(bindingResult.hasGlobalErrors()){
                bindingResult.rejectValue("endDate", "error.user", Constants.INVALID_DATE);
            }
            modelAndView.setViewName("habits/add");
            modelAndView.addObject("frequencies", Stream.of(HabitFrequencyEnum.values())
                    .map(HabitFrequencyEnum::getFrequencyName).collect(Collectors.toList()));
            return modelAndView;
        }

        User user = (User) authentication.getPrincipal();
        this.habitService.saveHabit(habitAddDTO, user);
        modelAndView.setViewName("redirect:/habits/all");
        return modelAndView;
    }
}
