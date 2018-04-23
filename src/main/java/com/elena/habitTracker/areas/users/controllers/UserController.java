package com.elena.habitTracker.areas.users.controllers;

import com.elena.habitTracker.areas.habits.services.HabitService;
import com.elena.habitTracker.areas.logs.annotations.Log;
import com.elena.habitTracker.areas.roles.enums.RoleEnum;
import com.elena.habitTracker.areas.users.entities.User;
import com.elena.habitTracker.areas.users.models.binding.UserEditBindingModel;
import com.elena.habitTracker.areas.users.models.binding.UserLoginBindingModel;
import com.elena.habitTracker.areas.users.models.binding.UserRegisterBindingModel;
import com.elena.habitTracker.areas.users.services.UserService;
import com.elena.habitTracker.areas.users.util.Constants;
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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {
    private final UserService userService;
    private final HabitService habitService;

    @Autowired
    public UserController(UserService userService, HabitService habitService) {
        this.userService = userService;
        this.habitService = habitService;
    }

    @GetMapping(path = "/register")
    @PreAuthorize("!isAuthenticated()")
    public ModelAndView register(@ModelAttribute("userRegisterModel") UserRegisterBindingModel userRegisterBindingModel) {
        return super.view("users/register",
                "userRegisterModel", userRegisterBindingModel);
    }

    @Log
    @PostMapping(path = "/register")
    @PreAuthorize("!isAuthenticated()")
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
    @PreAuthorize("!isAuthenticated()")
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

    @GetMapping("/{id}/statistics")
    @PreAuthorize("@accessService.hasAccess(authentication, #id)")
    public ModelAndView viewStatistics(@PathVariable Long id,
                                       @PageableDefault(size = ApplicationConstants.DEFAULT_STATISTICS_COUNT_PER_PAGE) Pageable pageable) {
        return super.view("users/statistics", "habitsPageModel",
                this.habitService.getHabitsPageByUser(this.userService.getUserById(id), pageable),
                "userId", id,
                "userViewModel", this.userService.getUserById(id),
                "page", pageable.getPageNumber());
    }

    @GetMapping("/statistics")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView viewStatistics(@PageableDefault(size = ApplicationConstants.DEFAULT_STATISTICS_COUNT_PER_PAGE) Pageable pageable,
                                       Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return this.viewStatistics(user.getId(), pageable);
    }

    @GetMapping(value = "/statistics/{id}", produces = "application/json")
    @PreAuthorize("@accessService.hasAccess(authentication, #id)")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String viewStatistics(@PathVariable Long id) {
        return this.habitService.extractHeatmapData(id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView all(@PageableDefault(size = ApplicationConstants.DEFAULT_VIEWS_COUNT_PER_PAGE) Pageable pageable) {
        return super.view("users/all", "usersPageModel", this.userService.getAllUsers(pageable),
                "page", pageable.getPageNumber());
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
    public ModelAndView edit(@Valid @ModelAttribute("userEditModel") UserEditBindingModel userEditBindingModel,
                             BindingResult bindingResult, Authentication authentication, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return this.edit(id);
        }

        User user = (User) authentication.getPrincipal();
        this.userService.editUser(id, userEditBindingModel);

        return id.equals(user.getId()) ? super.redirect("/users/logout") : super.redirect("/users/all");
    }

    @Log
    @PostMapping("/delete")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@RequestParam("id") Long id) {
        this.userService.deleteUser(id);
        return "/logs/all";
    }
}
