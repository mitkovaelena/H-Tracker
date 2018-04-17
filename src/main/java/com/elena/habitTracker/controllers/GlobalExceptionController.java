package com.elena.habitTracker.controllers;

import com.elena.habitTracker.errors.ResourceNotFoundException;
import com.elena.habitTracker.errors.UnauthorizedException;
import com.elena.habitTracker.util.ApplicationConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@ControllerAdvice
public class GlobalExceptionController extends BaseController {

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView getException(RuntimeException e) {
        String errorMessage =
                e.getClass().isAnnotationPresent(ResponseStatus.class)
                        ? e.getClass().getAnnotation(ResponseStatus.class).reason()
                        : ApplicationConstants.DEFAULT_ERROR_MESSAGE;

        return this.view("error/error-template", "error", errorMessage);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView unauthorized(){
      return this.getException(new UnauthorizedException());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView notFound(){
        return this.view("error/404");
    }

}
