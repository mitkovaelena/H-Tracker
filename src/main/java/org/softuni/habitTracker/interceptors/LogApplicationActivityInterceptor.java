package org.softuni.habitTracker.interceptors;

import org.softuni.habitTracker.areas.logs.annotations.Log;
import org.softuni.habitTracker.areas.logs.entities.ApplicationLog;
import org.softuni.habitTracker.areas.logs.services.LogService;
import org.softuni.habitTracker.areas.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class LogApplicationActivityInterceptor  extends HandlerInterceptorAdapter {
    private LogService logService;
    private UserService userService;


    @Autowired
    public LogApplicationActivityInterceptor(LogService logService, UserService userService) {
        this.logService = logService;
        this.userService = userService;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (handlerMethod.hasMethodAnnotation(Log.class)) {
                String message = String.format("%s - %s (id: %s)",
                        handlerMethod.getMethod().getName(),
                        handlerMethod.getBeanType().getSimpleName().toLowerCase().replace("controller", ""),
                        request.getRequestURI().substring(request.getRequestURI().lastIndexOf('/')+1));

                ApplicationLog log = new ApplicationLog(LocalDateTime.now(), message);
                if (request.getUserPrincipal() != null) {
                    log.setUser(this.userService.getByUsername(request.getUserPrincipal().getName()));
                }

                this.logService.saveLog(log);
            }
        }
    }
}
