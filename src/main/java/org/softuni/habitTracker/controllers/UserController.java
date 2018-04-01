package org.softuni.habitTracker.controllers;

import org.hibernate.validator.internal.constraintvalidators.bv.NotNullValidator;
import org.softuni.habitTracker.domain.entities.User;
import org.softuni.habitTracker.domain.models.binding.HabitEditDTO;
import org.softuni.habitTracker.domain.models.binding.UserEditDto;
import org.softuni.habitTracker.domain.models.binding.UserLoginDTO;
import org.softuni.habitTracker.domain.models.binding.UserRegisterDTO;
import org.softuni.habitTracker.domain.models.view.HabitViewDTO;
import org.softuni.habitTracker.domain.models.view.UserViewDto;
import org.softuni.habitTracker.services.UserService;
import org.softuni.habitTracker.util.Constants;
import org.softuni.habitTracker.util.enums.FrequencyEnum;
import org.softuni.habitTracker.util.enums.PriorityEnum;
import org.softuni.habitTracker.util.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.xml.validation.Validator;
import javax.xml.validation.ValidatorHandler;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView register(ModelAndView modelAndView, @ModelAttribute("userRegisterModel") UserRegisterDTO userRegisterDTO) {
        modelAndView.setViewName("users/register");
        modelAndView.addObject("userRegisterModel", userRegisterDTO);

        return modelAndView;
    }

    @PostMapping(path = "/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView register(ModelAndView modelAndView, @Valid @ModelAttribute("userRegisterModel") UserRegisterDTO userRegisterDTO,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            if(bindingResult.hasGlobalErrors()){
                bindingResult.rejectValue("confirmPassword", "error.user", Constants.PASSWORDS_MISMATCH);
            }
            modelAndView.setViewName("users/register");
            return modelAndView;
        }

        this.userService.saveUser(userRegisterDTO);
        modelAndView.setViewName("redirect:/users/login");
        return modelAndView;
    }

    @GetMapping(path = "/login")
    @PreAuthorize("isAnonymous()")
    public ModelAndView login(ModelAndView modelAndView, @ModelAttribute("userLoginModel") UserLoginDTO userLoginDTO,
                              @RequestParam(required = false) String error, BindingResult bindingResult) {
        if(error != null){
            bindingResult.rejectValue("username", "error.user", Constants.INCORRECT_USERNAME_OR_PASSWORD);
            bindingResult.rejectValue("password", "error.user",Constants.INCORRECT_USERNAME_OR_PASSWORD);
        }
        modelAndView.setViewName("users/login");
        modelAndView.addObject("userLoginModel", userLoginDTO);

        return modelAndView;
    }

    @GetMapping(path = "/logout")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView logout(ModelAndView modelAndView, HttpSession session) {
        session.invalidate();
        modelAndView.setViewName("redirect:/");

        return modelAndView;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView all(ModelAndView modelAndView, Authentication authentication) {
        modelAndView.setViewName("users/all");
        User user = (User) authentication.getPrincipal();

        List<UserViewDto> userViewDtos = this.userService.getAllUsers();
        modelAndView.addObject("userViews", userViewDtos);

        return modelAndView;
    }

    @GetMapping(path = "/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView edit(ModelAndView modelAndView, @PathVariable("id") Long id) {
        modelAndView.setViewName("users/edit");
        modelAndView.addObject("userEditModel", this.userService.getUserEditDtoById(id));
        modelAndView.addObject("username", this.userService.getUsernameById(id));
        modelAndView.addObject("privileges", Stream.of(RoleEnum.values())
                .map(RoleEnum::toString).map(String::toLowerCase).collect(Collectors.toList()));
        return modelAndView;
    }

    @PostMapping(path = "/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView edit(ModelAndView modelAndView, @Valid @ModelAttribute("userEditModel") UserEditDto userEditDto,
                             BindingResult bindingResult, Authentication authentication, @PathVariable("id") Long id) {

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("users/edit");
            modelAndView.addObject("username", this.userService.getUserEditDtoById(id));
            modelAndView.addObject("privileges", Stream.of(RoleEnum.values())
                    .map(RoleEnum::toString).collect(Collectors.toList()));
            return modelAndView;
        }

        this.userService.editUser(id, userEditDto);
        modelAndView.setViewName("redirect:/users/all");
        return modelAndView;
    }

    @PostMapping("delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView delete(ModelAndView modelAndView, @PathVariable("id") Long id) {
        this.userService.deleteUser(id);

        modelAndView.setViewName("redirect:/users/all");
        return modelAndView;
    }
}
