package org.softuni.habitTracker.areas.roles.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AccessController {

    @GetMapping("/unauthorized")
    public String unauthorized() {
        return "unauthorized";
    }

}