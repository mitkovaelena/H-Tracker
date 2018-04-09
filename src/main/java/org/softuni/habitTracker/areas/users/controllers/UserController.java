package org.softuni.habitTracker.areas.users.controllers;

import org.softuni.habitTracker.areas.habits.entities.Habit;
import org.softuni.habitTracker.areas.habits.services.HabitService;
import org.softuni.habitTracker.areas.logs.annotations.Log;
import org.softuni.habitTracker.areas.roles.enums.RoleEnum;
import org.softuni.habitTracker.areas.users.entities.User;
import org.softuni.habitTracker.areas.users.models.binding.UserEditBindingModel;
import org.softuni.habitTracker.areas.users.models.binding.UserLoginBindingModel;
import org.softuni.habitTracker.areas.users.models.binding.UserRegisterBindingModel;
import org.softuni.habitTracker.areas.users.services.UserService;
import org.softuni.habitTracker.areas.users.util.Constants;
import org.softuni.habitTracker.controllers.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {
    private final UserService userService;
    private final HabitService habitService; //ToDo

    @Autowired
    public UserController(UserService userService, HabitService habitService) {
        this.userService = userService;
        this.habitService = habitService;
    }

    @GetMapping(path = "/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView register(@ModelAttribute("userRegisterModel") UserRegisterBindingModel userRegisterBindingModel) {
        return super.view("users/register",
                "userRegisterModel", userRegisterBindingModel);
    }

    @Log
    @PostMapping(path = "/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView register(@Valid @ModelAttribute("userRegisterModel") UserRegisterBindingModel userRegisterBindingModel,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            if (bindingResult.hasGlobalErrors()) {
                bindingResult.rejectValue("confirmPassword", "error.user", Constants.PASSWORDS_MISMATCH);
            }
            return this.register(userRegisterBindingModel);
        }

        this.userService.saveUser(userRegisterBindingModel);
        return super.redirect("/users/login");
    }

    @GetMapping(path = "/login")
    @PreAuthorize("isAnonymous()")
    public ModelAndView login(@ModelAttribute("userLoginModel") UserLoginBindingModel userLoginBindingModel,
                              @RequestParam(required = false) String error, BindingResult bindingResult) {
        if (error != null) {
            bindingResult.rejectValue("username", "error.user", Constants.INCORRECT_USERNAME_OR_PASSWORD);
            bindingResult.rejectValue("password", "error.user", Constants.INCORRECT_USERNAME_OR_PASSWORD);
        }
        return super.view("users/login", "userLoginModel", userLoginBindingModel);
    }

    @GetMapping(path = "/logout")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView logout(HttpSession session) {
        session.invalidate();
        return super.redirect("/");
    }

    @GetMapping("/statistics")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView viewStatistics(Principal principal) {
        User user = this.userService.getByUsername(principal.getName());
        Map<Habit, String> habitViewModels = new TreeMap<>();
        for (Habit habit : user.getHabits()) {
            habitViewModels.put(habit, this.habitService.extractHeatmapData(habit));
        }

        return super.view("users/statistics", "habitViewModels", habitViewModels);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView all() {
        return super.view("users/all", "userViews", this.userService.getAllUsers());
    }

    @GetMapping(path = "/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView edit(@PathVariable("id") Long id) {
        return super.view("users/edit",
                "userEditModel", this.userService.getUserEditDtoById(id),
                "username", this.userService.getUsernameById(id),
                "privileges", Stream.of(RoleEnum.values())
                        .map(RoleEnum::toString).map(String::toLowerCase).collect(Collectors.toList()));
    }

    @Log
    @PostMapping(path = "/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView edit(ModelAndView modelAndView, @Valid @ModelAttribute("userEditModel") UserEditBindingModel userEditBindingModel,
                             BindingResult bindingResult, Authentication authentication, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return this.edit(id);
        }

        this.userService.editUser(id, userEditBindingModel);
        return super.redirect("/users/all");
    }

    @Log
    @PostMapping("delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView delete(@PathVariable("id") Long id) {
        this.userService.deleteUser(id);
        return super.redirect("/users/all");
    }
}
