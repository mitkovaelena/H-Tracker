package org.softuni.habitTracker.areas.roles.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessController {

    @GetMapping("/unauthorized")
    public String unauthorized() {
        return "unauthorized";
    }
}