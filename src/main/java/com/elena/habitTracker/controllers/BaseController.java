package com.elena.habitTracker.controllers;

import org.springframework.web.servlet.ModelAndView;

public abstract class BaseController {
    protected ModelAndView view(String view) {
        ModelAndView modelAndView = new ModelAndView(view);
        return modelAndView;
    }

    protected ModelAndView view(String view, Object...args) {
        ModelAndView modelAndView = new ModelAndView(view);
        for (int i = 0; i < args.length; i+=2) {
            modelAndView.addObject(String.valueOf(args[i]), args[i+1]);
        }
        return modelAndView;
    }

    protected ModelAndView redirect(String url) {
        return new ModelAndView("redirect:" + url);
    }
}
