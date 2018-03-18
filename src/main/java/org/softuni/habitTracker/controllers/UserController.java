package org.softuni.habitTracker.controllers;

import org.softuni.habitTracker.domain.models.binding.UserLoginDTO;
import org.softuni.habitTracker.domain.models.binding.UserRegisterDTO;
import org.softuni.habitTracker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {
    private UserService userService;

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
    public String register(@Valid @ModelAttribute("userRegisterModel") UserRegisterDTO userRegisterDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "users/register";
        }

        this.userService.saveUser(userRegisterDTO);
        return "redirect:/users/login";
    }

    @GetMapping(path = "/login")
    @PreAuthorize("isAnonymous()")
    public ModelAndView login(ModelAndView modelAndView, @ModelAttribute("userLoginModel") UserLoginDTO userLoginDTO, @RequestParam(required = false) String error) {
        if(error != null){
            System.out.println(error);
        }
        modelAndView.setViewName("users/login");
        modelAndView.addObject("userLoginModel", userLoginDTO);

        return modelAndView;
    }

//    @PostMapping(path = "/login")
//    @PreAuthorize("isAnonymous()")
//    public String login(@Valid @ModelAttribute("userLoginModel") UserLoginDTO userLoginDTO, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return "users/login";
//        }
//
//        //Login
//        return "redirect:/";
//    }

    @PostMapping(path = "/logout")
    @PreAuthorize("isAuthenticated()")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
