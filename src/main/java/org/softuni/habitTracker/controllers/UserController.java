package org.softuni.habitTracker.controllers;

import org.hibernate.validator.internal.constraintvalidators.bv.NotNullValidator;
import org.softuni.habitTracker.domain.models.binding.UserLoginDTO;
import org.softuni.habitTracker.domain.models.binding.UserRegisterDTO;
import org.softuni.habitTracker.services.UserService;
import org.softuni.habitTracker.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.List;

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
}
