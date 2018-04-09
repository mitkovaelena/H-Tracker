package org.softuni.habitTracker.areas.home.controllers;

import org.softuni.habitTracker.controllers.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController extends BaseController {

    @GetMapping("/")
    public ModelAndView index() {
        return super.view("index");
    }
}
